#!/bin/bash
DAY="06"

echo "Generating Day$DAY"
touch src/Day"$DAY".txt
touch src/Day"$DAY"_test.txt
TEMPLATE=$(cat src/DayXX.kt)
echo "${TEMPLATE//XX/$DAY}" > src/Day"$DAY".kt