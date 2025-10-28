package com.example.revicar_rgi.navigation

object AppRoutes {
    const val SPLASH_SCREEN = "splash_screen"
    const val LOGIN_SCREEN = "login_screen"
    const val REGISTER_SCREEN = "register_screen"

    const val MAIN_APP_SCREEN = "main_app_screen"

    const val BUYER_HOME_SCREEN = "buyer_home_screen"
    const val MECHANIC_HOME_SCREEN = "mechanic_home_screen"

    const val INSPECTIONS_SCREEN = "inspections_screen"
    const val NOTIFICATIONS_SCREEN = "notifications_screen"

    const val INSPECTION_FORM_SCREEN = "inspection_form_screen"

    const val BUYER_INSPECTION_DETAIL = "buyer_inspection_detail/{inspectionId}"
    const val MECHANIC_INSPECTION_DETAIL = "mechanic_inspection_detail/{inspectionId}"


    const val BUYER_INSPECTION_DETAIL_ROUTE = "buyer_inspection_detail"
    const val MECHANIC_INSPECTION_DETAIL_ROUTE = "mechanic_inspection_detail"
    const val INSPECTION_REPORT_SCREEN = "inspection_report/{inspectionId}"
    const val BUYER_REPORT_SCREEN = "buyer_report/{inspectionId}"
    const val BUYER_REPORT_ROUTE = "buyer_report"
    const val PROFILE_SCREEN = "profile_screen"
    const val MECHANIC_REPORT_SCREEN = "mechanic_report/{inspectionId}"
    const val MECHANIC_REPORT_ROUTE = "mechanic_report"
}