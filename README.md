# x509-generator 

### what you need to know

this util will generate for you:
- a keypair (prime256v1)
- a self-signed x509 certificate with a configurable CN

#### certificate and private key are stored in pem format in the working directory

#### the CN (=common name is the first argument of the program)

### Run

#### Java

- run the application and pass the first argument as your desired common name

#### Gradle
```bash 
  gradlew run --args="<common-name>"
```

the files are saved in `build/content`