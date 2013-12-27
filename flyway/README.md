SQL database migrations
=======================

Each migration should be saved into it's own file in the sql/ folder. The file name consists of:
 * **V** - prefix
 * **version**, underscores separate parts
 * **__** - separator, two underscores
 * **description**
 * **.sql** - suffix

Version number must be **unique**. As of now migrations have version of `1_0_yyyyMMdd`, if there are multiple migrations in the same day they should be disambiguated by adding numeric suffix, like `_1`.

Final naming convention:

V1_0_YYYYMMDD_N__description.sql
N - optional if there are several patches in one day.

Examples:
V1_0_20130523__datev_view_interim.sql
V1_0_20130525_1__icr3335_order_reports_by_date.sql
V1_0_20130525_2__icr3335_add_super_group_to_order_view.sql


See http://flywaydb.org/documentation/migration/sql.html for more information.

Use "./flyway.sh migrate" to execute migrations
