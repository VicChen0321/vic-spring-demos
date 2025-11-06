#!/bin/bash

API_URL="http://localhost:8080/api/users"

# 建立新 user
curl -X POST "$API_URL" \
  -H "Content-Type: application/json" \
  -d 'Alice'
echo
