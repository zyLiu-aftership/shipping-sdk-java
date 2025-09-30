# AfterShip Shipping API library for Java

This library allows you to quickly and easily use the AfterShip Shipping API via Java.

For updates to this library, see our [GitHub release page](https://github.com/AfterShip/shipping-sdk-java/releases).

If you need support using AfterShip products, please contact support@aftership.com.

## Table of Contents

- [AfterShip Shipping API library for Java](#aftership-shipping-api-library-for-java)
  - [Table of Contents](#table-of-contents)
  - [Before you begin](#before-you-begin)
  - [Quick Start](#quick-start)
    - [Installation](#installation)
  - [Constructor](#constructor)
    - [Example](#example)
  - [Rate Limiter](#rate-limiter)
  - [Error Handling](#error-handling)
    - [Error List](#error-list)
  - [Endpoints](#endpoints)
    - [/locations](#locations)
    - [/rates](#rates)
    - [/shipper-accounts](#shipper-accounts)
    - [/labels](#labels)
    - [/couriers](#couriers)
    - [/cancel-labels](#cancel-labels)
    - [/address-validations](#address-validations)
    - [/manifests](#manifests)
    - [/pickups](#pickups)
    - [/cancel-pickups](#cancel-pickups)
  - [Help](#help)
  - [License](#license)


## Before you begin

Before you begin to integrate:

- [Create an AfterShip account](https://admin.aftership.com/).
- [Create an API key](https://organization.automizely.com/api-keys).
- [Install Java](https://www.oracle.com/java/technologies/downloads/) version Java 1.8 or later.

## Quick Start

### Installation
```bash
<dependency>
    <groupId>com.aftership</groupId>
    <artifactId>shipping-sdk</artifactId>
    <version>3.0.1</version>
</dependency>
```


## Constructor

Create AfterShip instance with options

| Name       | Type   | Required | Description                                                                                                                       |
| ---------- | ------ | -------- | --------------------------------------------------------------------------------------------------------------------------------- |
| api_key    | string | ✔        | Your AfterShip API key                                                                                                            |
| auth_type  | enum   |          | Default value: `AuthType.API_KEY` <br > AES authentication: `AuthType.AES` <br > RSA authentication: `AuthType.RSA`               |
| api_secret | string |          | Required if the authentication type is `AuthType.AES` or `AuthType.RSA`                                                           |
| domain     | string |          | AfterShip API domain. Default value: https://sandbox-api.aftership.com                                                                    |
| user_agent | string |          | User-defined user-agent string, please follow [RFC9110](https://www.rfc-editor.org/rfc/rfc9110#field.user-agent) format standard. |
| proxy      | string |          | HTTP proxy URL to use for requests. <br > Default value: `null` <br > Example: `http://192.168.0.100:8888`                        |
| max_retry  | number |          | Number of retries for each request. Default value: 2. Min is 0, Max is 10.                                                        |
| timeout    | number |          | Timeout for each request in milliseconds.                                                                                         |

### Example

```java
import com.aftership.shipping.ShippingSdk;
import com.aftership.shipping.model.PostLabelsRequest;
import com.aftership.shipping.model.PostLabelsResponse;
import com.aftership.shipping.labels.LabelsResource;

public class App {
    public static void main(String[] args) {
        try {
            ShippingSdk.init(
                    "YOUR_API_KEY"
            );
            PostLabelsRequest request = new PostLabelsRequest();
            PostLabelsResponse response = LabelsResource.postLabels()
                .setPostLabelsRequest(request)
                .create();
            System.out.println(response.getData());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

## Rate Limiter

See the [Rate Limit](https://www.aftership.com/docs/shipping/quickstart/rate-limit) to understand the AfterShip rate limit policy.

## Error Handling

The SDK will return an error object when there is any error during the request, with the following specification:

| Name            | Type   | Description                    |
| --------------- | ------ | ------------------------------ |
| message         | string | Detail message of the error    |
| code            | enum   | Error code enum for API Error. |
| meta_code       | number | API response meta code.        |
| status_code     | number | HTTP status code.              |
| response_body   | string | API response body.             |
| response_header | object | API response header.           |


### Error List

| code                              | meta_code       | status_code     | message                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                |
| --------------------------------- | --------------- | --------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| OK | 200 | 200 | OK |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| UNAUTHORIZED | 401 | 401 | Invalid API key. |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| TOO_MANY_REQUESTS | 429 | 429 | You have exceeded the API call rate limit. Please check the header field &#39;X-RateLimit-Reset&#39; for time left until the limit release. |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| INTERNAL_SERVER_ERROR | 500 | 500 | Something went wrong on AfterShip Shipping&#39;s end. |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| BAD_GATEWAY | 502 | 502 | Something went wrong on AfterShip Shipping&#39;s end. |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| SERVICE_UNAVAILABLE | 503 | 503 | Something went wrong on AfterShip Shipping&#39;s end. |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| GATEWAY_TIMEOUT | 504 | 504 | Something went wrong on AfterShip Shipping&#39;s end. |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| PENDING | 3001 | 200 | The request has been accepted for processing, but the processing has not been completed. |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| INTERNAL_ERROR | 4100 | 200 | Internal Error. |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| INTERNAL_ERROR_RETRY | 4101 | 200 | Internal Error, please try again. |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| BAD_REQUEST | 4104 | 200 | The request was invalid or cannot be otherwise served. |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| INVALID_JSON | 4109 | 200 | Invalid JSON. |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| SHIPPER_ACCOUNT_NOT_FOUND | 4140 | 200 | Shipper account not found. |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| RECORD_NOT_FOUND | 4153 | 200 | Item does not exist. |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| SHIPPER_ACCOUNT_LOCKED | 4155 | 200 | Access to shipper_account locked during manifest/cancel-label operation. |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| RATE_NOT_FOUND | 4157 | 200 | Rate is not found |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| PREPAID_ACCOUNT_DISABLED | 4159 | 200 | The prepaid account is currently deactivated, please try again later. |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| PAYMENT_CARD_ERROR | 4161 | 200 | Your card is declined by payment gateway. |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| PAYMENT_API_ERROR | 4162 | 200 | There is an error when connecting to payment gateway. |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| PAYMENT_INVALID_REQUEST_AMOUNT_ERROR | 4163 | 200 | The amount of given transaction is invalid. |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| PAYMENT_INVALID_REQUEST_ERROR | 4164 | 200 | The payment request is invalid. |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| OPERATION_NOT_ALLOWED_COURIER | 4171 | 200 | Operation is not allowed on this courier. |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| TOO_MANY_CUSTOM_REQUESTS | 4172 | 200 | You have exceeded the limit on number of requests, please try again later |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| RESPONSE_ERROR | 4703 | 200 | The courier seems to be currently unavailable, please try again later. |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| NO_RESPONSE | 4705 | 200 | No response was returned. |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| RATE_REQUEST_FAILED | 4713 | 200 | All or partial failed in rate request. |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| COURIER_ERROR | 4715 | 200 | The request is invalid or cannot be served by courier. |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| ADDRESS_TOO_LONG | 4716 | 200 | The address length is too long. |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| ALREADY_CANCELED | 4722 | 200 | The tracking number is already canceled, it can not be canceled again. |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| DOES_NOT_EXIST | 4723 | 200 | The tracking number does not exist, can not not be canceled. |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| ALREADY_MANIFESTED | 4724 | 200 | The tracking number is manifested, so can not be canceled. |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| NO_SHIPMENTS_MANIFEST | 4725 | 200 | There is no shipments to manifest. |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| NO_AVAILABLE_NUMBER | 4730 | 200 | No more pre-assigned tracking numbers available in this shipper account. Please input the new tracking numbers in your shipper account setting page. |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| CREDENTIALS_ERROR | 4732 | 200 | The credential error, please check your shipper account. |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| PAYLOAD_ERROR | 4733 | 200 | The request is invalid or cannot be served by AfterShip Shipping. |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| DUPLICATE_ORDER_NUMBER | 4801 | 200 | Duplicate order_number, please specify a different order_number and try again. |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| UNAVAILABLE_SERVICE_TYPE | 4802 | 200 | The service_type is not available for this shipment. |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| UNAVAILABLE_SERVICE_TYPE_SERVICE_OPTION | 4803 | 200 | The service_type and service_option are not available for this shipment. |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| MAX_WEIGHT | 4804 | 200 | The weight is not available for this shipment. |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |

## Endpoints

The AfterShip SDK has the following resource which are exactly the same as the API endpoints:

- LocationsResource
  - Get locations
- RatesResource
  - Get rates
  - Calculate rates
  - Get a rate
- ShipperAccountsResource
  - Get shipper accounts
  - Create a shipper account
  - Get a shipper account
  - Delete a shipper account
  - Update a shipper account&#39;s information
  - Update a shipper account&#39;s credentials
  - Update a shipper account&#39;s settings
- LabelsResource
  - Get labels
  - Create a label
  - Get a label
- CouriersResource
  - Get all couriers
- CancelLabelsResource
  - Get the cancelled labels
  - Cancel a label
  - Get a cancelled label
- AddressValidationsBetaResource
  - Create an address validation
- ManifestsResource
  - Get manifests
  - Create a manifest
  - Get a manifest
- PickupsResource
  - Get pickups
  - Create a pickup
  - Get a pickup
- CancelPickupsResource
  - Get the cancelled pickups
  - Cancel a pickup
  - Get a cancelled pickup

### /locations
**GET** /locations

```java
    GetLocationsResponse response = LocationsResource.getLocations()
        .fetch();
    System.out.println(response.getData());
```

### /rates
**GET** /rates

```java
    GetRatesResponse response = RatesResource.getRates()
        .fetch();
    System.out.println(response.getData());
```

**POST** /rates

```java
    PostRatesRequest request = new PostRatesRequest();
    PostRatesResponse response = RatesResource.postRates()
        .setPostRatesRequest(request)
        .create();
    System.out.println(response.getData());
```

**GET** /rates/{id}

```java
    GetRateResponse response = RatesResource.getRate()
        .setId("valid_value")
        .fetch();
    System.out.println(response.getData());
```

### /shipper-accounts
**GET** /shipper-accounts

```java
    GetShipperAccountsResponse response = ShipperAccountsResource.getShipperAccounts()
        .fetch();
    System.out.println(response.getData());
```

**POST** /shipper-accounts

```java
    PostShipperAccountsRequest request = new PostShipperAccountsRequest();
    PostShipperAccountsResponse response = ShipperAccountsResource.postShipperAccounts()
        .setPostShipperAccountsRequest(request)
        .create();
    System.out.println(response.getData());
```

**GET** /shipper-accounts/{id}

```java
    GetShipperAccountsIdResponse response = ShipperAccountsResource.getShipperAccountsId()
        .setId("valid_value")
        .fetch();
    System.out.println(response.getData());
```

**DELETE** /shipper-accounts/{id}

```java
    DeleteShipperAccountsIdResponse response = ShipperAccountsResource.deleteShipperAccountsId()
        .setId("valid_value")
        .delete();
    System.out.println(response.getData());
```

**PUT** /shipper-accounts/{id}/info

```java
    PutShipperAccountsIdInfoRequest request = new PutShipperAccountsIdInfoRequest();
    PutShipperAccountsIdInfoResponse response = ShipperAccountsResource.putShipperAccountsIdInfo()
        .setId("valid_value")
        .setPutShipperAccountsIdInfoRequest(request)
        .update();
    System.out.println(response.getData());
```

**PATCH** /shipper-accounts/{id}/credentials

```java
    PatchShipperAccountsIdCredentialsRequest request = new PatchShipperAccountsIdCredentialsRequest();
    PatchShipperAccountsIdCredentialsResponse response = ShipperAccountsResource.patchShipperAccountsIdCredentials()
        .setId("valid_value")
        .setPatchShipperAccountsIdCredentialsRequest(request)
        .update();
    System.out.println(response.getData());
```

**PATCH** /shipper-accounts/{id}/settings

```java
    PatchShipperAccountsIdSettingsRequest request = new PatchShipperAccountsIdSettingsRequest();
    PatchShipperAccountsIdSettingsResponse response = ShipperAccountsResource.patchShipperAccountsIdSettings()
        .setId("valid_value")
        .setPatchShipperAccountsIdSettingsRequest(request)
        .update();
    System.out.println(response.getData());
```

### /labels
**GET** /labels

```java
    GetLabelsResponse response = LabelsResource.getLabels()
        .fetch();
    System.out.println(response.getData());
```

**POST** /labels

```java
    PostLabelsRequest request = new PostLabelsRequest();
    PostLabelsResponse response = LabelsResource.postLabels()
        .setPostLabelsRequest(request)
        .create();
    System.out.println(response.getData());
```

**GET** /labels/{id}

```java
    GetLabelResponse response = LabelsResource.getLabel()
        .setId("valid_value")
        .fetch();
    System.out.println(response.getData());
```

### /couriers
**GET** /couriers

```java
    GetCouriersResponse response = CouriersResource.getCouriers()
        .fetch();
    System.out.println(response.getData());
```

### /cancel-labels
**GET** /cancel-labels

```java
    GetCancelLabelsResponse response = CancelLabelsResource.getCancelLabels()
        .fetch();
    System.out.println(response.getData());
```

**POST** /cancel-labels

```java
    PostCancelLabelsRequest request = new PostCancelLabelsRequest();
    PostCancelLabelsResponse response = CancelLabelsResource.postCancelLabels()
        .setPostCancelLabelsRequest(request)
        .create();
    System.out.println(response.getData());
```

**GET** /cancel-labels/{id}

```java
    GetCancelLabelResponse response = CancelLabelsResource.getCancelLabel()
        .setId("valid_value")
        .fetch();
    System.out.println(response.getData());
```

### /address-validations
**POST** /address-validations

```java
    PostAddressValidationsRequest request = new PostAddressValidationsRequest();
    PostAddressValidationsResponse response = AddressValidationsBetaResource.postAddressValidations()
        .setPostAddressValidationsRequest(request)
        .create();
    System.out.println(response.getData());
```

### /manifests
**GET** /manifests

```java
    GetManifestsResponse response = ManifestsResource.getManifests()
        .fetch();
    System.out.println(response.getData());
```

**POST** /manifests

```java
    PostManifestsRequest request = new PostManifestsRequest();
    PostManifestsResponse response = ManifestsResource.postManifests()
        .setPostManifestsRequest(request)
        .create();
    System.out.println(response.getData());
```

**GET** /manifests/{id}

```java
    GetManifestResponse response = ManifestsResource.getManifest()
        .setId("valid_value")
        .fetch();
    System.out.println(response.getData());
```

### /pickups
**GET** /pickups

```java
    GetPickupsResponse response = PickupsResource.getPickups()
        .fetch();
    System.out.println(response.getData());
```

**POST** /pickups

```java
    PostPickupsRequest request = new PostPickupsRequest();
    request.setPickupStartTime("valid_value");
    request.setPickupEndTime("valid_value");
    AddressV3 pickupFrom = new AddressV3();
    request.setPickupFrom(pickupFrom);
    PostPickupsResponse response = PickupsResource.postPickups()
        .setPostPickupsRequest(request)
        .create();
    System.out.println(response.getData());
```

**GET** /pickups/{id}

```java
    GetPickupResponse response = PickupsResource.getPickup()
        .setId("valid_value")
        .fetch();
    System.out.println(response.getData());
```

### /cancel-pickups
**GET** /cancel-pickups

```java
    GetCancelPickupsResponse response = CancelPickupsResource.getCancelPickups()
        .fetch();
    System.out.println(response.getData());
```

**POST** /cancel-pickups

```java
    PostCancelPickupsRequest request = new PostCancelPickupsRequest();
    PostCancelPickupsRequestPickup pickup = new PostCancelPickupsRequestPickup();
    request.setPickup(pickup);
    PostCancelPickupsResponse response = CancelPickupsResource.postCancelPickups()
        .setPostCancelPickupsRequest(request)
        .create();
    System.out.println(response.getData());
```

**GET** /cancel-pickups/{id}

```java
    GetCancelPickupResponse response = CancelPickupsResource.getCancelPickup()
        .setId("valid_value")
        .fetch();
    System.out.println(response.getData());
```


## Help

If you get stuck, we're here to help:

- [Issue Tracker](https://github.com/AfterShip/shipping-sdk-java/issues) for questions, feature requests, bug reports and general discussion related to this package. Try searching before you create a new issue.
- Contact AfterShip official support via support@aftership.com

## License
Copyright (c) 2025 AfterShip

Licensed under the MIT license.