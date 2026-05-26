# QuickStartKS

Android POS SDK Demo for Kozen Financial/Component SDK.

## SDK Version

| SDK | Version |
|-----|---------|
| FinancialLib | 1.2.2_release |
| ComponentLib | 1.2.4_release |
| com.pos.sdk | release |

## Build Environment

- **Compile SDK**: 34
- **Min SDK**: 24
- **Target SDK**: 34
- **Java Version**: 1.8
- **Gradle**: 8.6

## Supported Devices

- K1211 (with secondary screen)
- K1352
- K1141
- L200
- P17
- P12 series
- P13
- P3
- D300

## Permissions Required

```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.CHANGE_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
```

## Project Structure

```
QuickStartKS/
├── app/
│   └── src/main/
│       ├── java/com/kozen/quickstartks/
│       │   ├── TransInitActivity.java      # Entry point, SDK initialization
│       │   ├── TransSelectActivity.java    # Transaction type selection
│       │   ├── TransActivity.java          # EMV transaction flow
│       │   ├── TransResultActivity.java    # Transaction result display
│       │   ├── QrPaymentActivity.java       # QR code payment (server mode)
│       │   ├── ScanPaymentActivity.java    # QR code scan
│       │   ├── TransScanResultActivity.java# QR transaction result
│       │   ├── BaseActivity.java           # Base activity
│       │   └── emv/
│       │       ├── EmvListenerImpl.java        # EMV callback (Financial SDK)
│       │       └── EmvListenerImplPOI.java     # EMV callback (POI SDK)
│       └── res/
│           └── layout/                    # UI layouts
├── libs/
│   ├── FinancialLib_1.2.2_release.aar
│   ├── ComponentLib_1.2.4_release.aar
│   ├── com.pos.sdk_release.jar
│   └── TerminalManagerLib_1.1.0_release.aar
```

## Key Flows

### 1. SDK Initialization
- Checks if `com.kozen.financial.service` exists → uses Financial SDK
- Checks if `com.kozen.component_service` exists → uses Component SDK
- Initializes keys via `ParameterInit.initKey()` or `ParameterInitPOI.initKey()`
- Initializes EMV config via `initEMVConifg()`

### 2. EMV Transaction Flow
1. User enters amount
2. Select transaction type (Card/QR)
3. Start EMV transaction via `emvManager.startTransaction()`
4. Handle callbacks:
   - `onEmvProcess()` - Card detection
   - `onSelectApplication()` - App selection
   - `onRequestInputPin()` - PIN entry
   - `onRequestOnlineProcess()` - Online authorization
   - `onTransactionResult()` - Final result

### 3. QR Payment Flow
1. Generate QR code with local IP address
2. Start HTTP server to receive payment notification
3. Monitor network for payment confirmation
4. Show result

## Error Codes

| Code | Constant | Description |
|------|----------|-------------|
| 0 | SUCCESS | Transaction approved |
| -1 | FAIL | Transaction failed |
| 1 | TIMEOUT | Transaction timeout |
| 2 | CANCEL | User cancelled |

### Printer Error Codes

| Error Code | Description |
|------------|-------------|
| POIPrinterManager.ERROR_NO_PAPER | Printer out of paper |
| ConstantPrinter.STATUS_NO_PAPER | Printer out of paper |

### EMV Error Codes

| Code | Description |
|------|-------------|
| EMV_APPROVED | EMV transaction approved |
| EMV_TIMEOUT | Transaction timeout |
| EMV_CANCEL | User cancelled |
| EMV_FALLBACK | Fallback to magstripe |
| EMV_MULTI_CONTACTLESS | Multiple contactless cards detected |

## Build Instructions

```bash
./gradlew clean
./gradlew assembleDebug
```

The APK will be generated at:
```
app/build/outputs/apk/debug/app-debug.apk
```

## Notes

- SDK keys in `ParameterInit.java` and `ParameterInitPOI.java` are for DEMO only
- Production environment must obtain keys through secure channels
- Card numbers are masked in display (e.g., `**** **** **** 1234`)
- PIN is never logged or displayed in plain text