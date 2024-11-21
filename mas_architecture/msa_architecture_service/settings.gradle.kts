rootProject.name = "msa_architecture_service"
include("order_service")
include("order_service:order-application")
include("order_service:order-container")
include("order_service:order-data-access")
include("order_service:order-domain")
include("order_service:order-messaging")