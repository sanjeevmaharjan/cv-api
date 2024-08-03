[Source](https://techdocs.akamai.com/iot-token-access-control/docs/generate-ecdsa-keys)

This procedure explains how to generate a pair of ECDSA keys with the NIST P-256 (prime256v1) curve that you can use to sign
and verify your JWTs.

1. Create a private key.

`openssl ecparam -name prime256v1 -genkey -noout -out ec-prime256v1-priv-key.pem`

Sample contents of the ec-prime256v1-priv-key.pem private key in PEM format:

```
-----BEGIN EC PRIVATE KEY-----
MHcCAQEEIC3x3wuxu3Jbvd3SVxOKvA91pnn8xv5aV2FHf/PT/U0WoAoGCCqGSM49
AwEHoUQDQgAEAwNo/Va4kQAVeDp9hEZieCKpsjCaZCqNEIAg66Zkw3Lwm86adfDf
MjHVZZkWZ8Ao+U8xCYYWnDeKuK9JJf1bzQ==
-----END EC PRIVATE KEY-----
```

2. Create a public key by extracting it from the private key.

`openssl ec -in ec-prime256v1-priv-key.pem -pubout > ec-prime256v1-pub-key.pem`

Sample contents of the ec-prime256v1-pub-key.pem public key in PEM format:

```
-----BEGIN PUBLIC KEY-----
MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEAwNo/Va4kQAVeDp9hEZieCKpsjCa
ZCqNEIAg66Zkw3Lwm86adfDfMjHVZZkWZ8Ao+U8xCYYWnDeKuK9JJf1bzQ==
-----END PUBLIC KEY-----
```

3. Combine the two keys

`cat ec-prime256v1-priv-key.pem ec-prime256v1-pub-key.pem > keypair.pem`

Sample contents of the keypair.pem keypair in PEM format:

```
-----BEGIN PUBLIC KEY-----
MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEAwNo/Va4kQAVeDp9hEZieCKpsjCa
ZCqNEIAg66Zkw3Lwm86adfDfMjHVZZkWZ8Ao+U8xCYYWnDeKuK9JJf1bzQ==
-----END PUBLIC KEY-----
-----BEGIN EC PRIVATE KEY-----
MHcCAQEEIC3x3wuxu3Jbvd3SVxOKvA91pnn8xv5aV2FHf/PT/U0WoAoGCCqGSM49
AwEHoUQDQgAEAwNo/Va4kQAVeDp9hEZieCKpsjCaZCqNEIAg66Zkw3Lwm86adfDf
MjHVZZkWZ8Ao+U8xCYYWnDeKuK9JJf1bzQ==
-----END EC PRIVATE KEY-----
```
