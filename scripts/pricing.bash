reset;clear;

echo
echo "top level callisto info"
echo

echo
curl localhost:8888/security/callisto
echo

echo
echo "current single pricing for callisto"
echo

echo
curl localhost:8888/security/pricing/callisto
echo

echo
echo "historical pricing for callisto"
echo

echo
curl localhost:8888/security/history/callisto
echo
