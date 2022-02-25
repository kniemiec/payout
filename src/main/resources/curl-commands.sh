######################### TRANSFER ##################################

## create transfer
curl -X POST "http://localhost:8080/start-transfer" -H "Content-Type: application/json" -d @start-transfer.json


## get transfer status
curl "http://localhost:8080/transfer-status/b17d6200-6b59-4ded-b025-0ce2c1a024c6"




##################### PAYOUT ##########################

# create payout
curl -v -X POST "http://localhost:8091/v1/topup" -H "Content-Type: application/json" -d @payout-data.json

