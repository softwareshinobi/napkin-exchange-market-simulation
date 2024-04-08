# Calculator API > /calculator/add > Add Two Numbers Endpoint

This endpoint adds two numbers together.

## Endpoint Request

Method: `GET`

URL: `/calculator/add`

## Request Body

```json
{
  "num1": 5,
  "num2": 3
}
```

## Request Parameters

| parameter | is required | detail |
|-----------|-------------|--------|
| num1 | required | The first number to add. Must be a valid integer or decimal. |
| num2 | required | The second number to add. Must be a valid integer or decimal. |

## Example Request:

```bash
curl -X POST 
    -H "Content-Type: application/json"
    -d '{"num1": 5, "num2": 3}' http://localhost:8080/calculator/add
```

## Response / 200 / Success 

Code: 

`200 OK`

Content:

```json
{
  "result": 8
}
```

## Response / 400 / Bad Request
 
Code: 

`400 Bad Request`

Content:

```json
{
  "error": "Invalid input parameters"
}
```

## Possible Errors Causes

    400 Bad Request: If one or both of the query parameters are missing or invalid.

    500 Internal Server Error: If an unexpected error occurs during the calculation.

## Additional Information

    This endpoint is intended for simple addition calculations.

    For more complex mathematical operations, consider using the Analytics API.
