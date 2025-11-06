#!/bin/bash

API_URL="http://localhost:8080/api/users"

# 取得 user list
curl -X GET "$API_URL"
echo
