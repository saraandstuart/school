package com.shannoncode.school.component;

import com.shannoncode.school.model.Profile;
import com.shannoncode.school.repository.ProfileRepository;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.ExpressionJwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfileSyncConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final ProfileRepository profileRepository;

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        String identityId = jwt.getSubject();

        profileRepository.findById(identityId).orElseGet(() -> {
            Profile newProfile = new Profile();
            newProfile.setId(identityId);
            newProfile.setDisplayName(jwt.getClaimAsString("preferred_username"));
            return profileRepository.save(newProfile);
        });

        Collection<GrantedAuthority> authorities = extractAuthorities(jwt);
        return new JwtAuthenticationToken(jwt, authorities);
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        var converter = new ExpressionJwtGrantedAuthoritiesConverter(
            new SpelExpressionParser().parseRaw("[realm_access][roles]")
        );

        converter.setAuthorityPrefix("ROLE_");

        return converter.convert(jwt);
    }
}
