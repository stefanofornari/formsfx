# Change Log

## 26.0.3 - 2026-06-07
[Full Changelog](https://github.com/dlsc-software-consulting-gmbh/formsfx/compare/26.0.2...26.0.3)

**Implemented enhancements:**

- Added `IntegerValidator` for validating string inputs as integers (with optional null/empty support).
- Migrated `IntegerField` and `Field` to use `commonsfx` `IntegerProperty` for improved null-safety.
- Added null-safety checks in `IntegerSliderControl` and `SimpleIntegerControl`.

**Other changes:**

- Added `commonsfx` dependency (v0.0.1-SNAPSHOT).
- Updated README.md with `IntegerValidator` documentation.
- Fixed CSS style for textarea highlight fill (`-fx-highlight-fill: -color-accent-emphasis`).
- Added comprehensive test coverage for `IntegerValidator`.

## 26.0.2 - 2026-05-31
[Full Changelog](https://github.com/dlsc-software-consulting-gmbh/formsfx/compare/26.0.1...26.0.2)

**Implemented enhancements:**

- Introduced `FieldTooltip` for improved tooltip management in controls.
- Enhanced `FieldEvent` types for better uniqueness by including class names.
- Added `java.logging` module requirement.
- Added `labelSpan()` to control how much space labels shall take in the grid.

**Other changes:**

- Updated project version to `26.0.2`.

## v26.0.0 - 2026-04-05
- Forked and published as com.github.stefanofornari:formsfx
