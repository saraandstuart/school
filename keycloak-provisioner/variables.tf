variable "keycloak_url" {
  type    = string
  default = "http://keycloak:11000"
}
variable "keycloak_admin_user" {
  type    = string
  default = "admin"
}
variable "keycloak_admin_password" {
  type      = string
  sensitive = true
}
variable "school_api_client_secret" {
  type      = string
  sensitive = true
}
variable "default_user_password" {
  type      = string
  sensitive = true
}
