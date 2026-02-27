package com.shannoncode.school.modules.profile;

import com.shannoncode.school.modules.enrollment.Enrollment;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Data;

@Entity
@Table(name = "profile")
@Data
public class Profile {
    @Id
    private String id; // Matches the 'sub' claim from IAM

    private String displayName;
    private String bio;
    private String avatarUrl;

    @OneToMany(mappedBy = "profile")
    private List<Enrollment> enrollments;
}
