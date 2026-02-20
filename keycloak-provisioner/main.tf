resource "keycloak_realm" "school" {
  realm   = "school"
  enabled = true
}

resource "keycloak_role" "admin" {
  realm_id = keycloak_realm.school.id
  name     = "ADMIN"
}

resource "keycloak_role" "student" {
  realm_id = keycloak_realm.school.id
  name     = "STUDENT"
}

resource "keycloak_user" "student_user" {
  realm_id   = keycloak_realm.school.id
  username   = "student1"
  enabled    = true
  email      = "shannoncodelimited+student1@gmail.com"
  first_name = "Student"
  last_name  = "One"

  initial_password {
    value     = var.default_user_password
    temporary = false
  }
}

resource "keycloak_user" "admin_user" {
  realm_id   = keycloak_realm.school.id
  username   = "admin1"
  enabled    = true
  email      = "shannoncodelimited+admin1@gmail.com"
  first_name = "Admin"
  last_name  = "One"

  initial_password {
    value     = var.default_user_password
    temporary = false
  }
}

resource "keycloak_user_roles" "student_roles" {
  realm_id = keycloak_realm.school.id
  user_id  = keycloak_user.student_user.id

  role_ids = [
    keycloak_role.student.id
  ]
}

resource "keycloak_user_roles" "admin_roles" {
  realm_id = keycloak_realm.school.id
  user_id  = keycloak_user.admin_user.id

  role_ids = [
    keycloak_role.admin.id
  ]
}

resource "keycloak_openid_client" "school_api" {
  realm_id                     = keycloak_realm.school.id
  client_id                    = "school-api"
  name                         = "school-api"
  enabled                      = true
  access_type                  = "CONFIDENTIAL"
  client_secret                = var.school_api_client_secret
  standard_flow_enabled        = true
  direct_access_grants_enabled = true

  valid_redirect_uris = [
    "http://localhost:8080/*"
  ]
}
