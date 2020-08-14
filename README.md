# AdWordsTest

An API to generate random costs for daily budget

URI: /generateCosts

Type: POST

Body: JSON with list of budgets periods

Eg.

    `[
        { "startDate": "2019-01-01T10:00:00", "endDate": "2019-01-01T11:00:00", "amount": 7},
        { "startDate": "2019-01-01T11:00:00", "endDate": "2019-01-01T12:00:00", "amount": 0},
        { "startDate": "2019-01-01T12:00:00", "endDate": "2019-01-01T23:00:00", "amount": 1},
        { "startDate": "2019-01-01T23:00:00", "endDate": "2019-01-05T10:00:00", "amount": 6},
        { "startDate": "2019-01-05T10:00:00", "endDate": "2019-01-06T00:00:00", "amount": 2},
        { "startDate": "2019-01-06T00:00:00", "endDate": "2019-02-09T13:13:00", "amount": 0},
        { "startDate": "2019-02-09T13:13:00", "endDate": "2019-03-01T12:00:00", "amount": 1},
        { "startDate": "2019-03-01T12:00:00", "endDate": "2019-03-01T14:00:00", "amount": 7},
        { "startDate": "2019-03-01T14:00:00", "endDate": null, "amount": 7}
    ]`
    
Header: (optional)  key: months value: Integer ( number of months to generate)

The result is a JSON with daily max budget and generated costs