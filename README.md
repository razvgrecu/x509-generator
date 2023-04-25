# Certificate Generator (X-509) and Azure DPS Enrollment Utility


## Usage

### Configuration
- there is an application.yaml
- you can create your own profile, for example a local profile (application-local.yml is already gitignored so you can create it safely locally)
  - make sure your run config/app starts with that profile too
#### Certificate configuration
- update the `crypto.outputFolder` property to specify where you would like the files to be stored.
- you can update the algorithm and the expiration year for the certificates
### Azure Configuration
- update the `azure.connection` property to specify the connection string to the azure dps (can be found on azure)
### API Endpoints
- `/api/v1/certificates`
  - creates the certificate and pk and returns a json with the pem content of both files
  - parameter is the commonName i.e. the name of the device
- `/api/v1/enrollment`
  - creates the certificate and pk and uses it to make an enrollment on azure and returns everything back
  - parameter is the commonName i.e. the name of the device
- `/api/v2/certificates`
  - creates the certificate and pk and returns them in a zip file
  - parameter is the commonName i.e. the name of the device
- `/api/v2/enrollment`
  - creates the enrollment on Azure based on a certificate, pk and commonName
  - parameters are certificateContent, privateKeyContent and commonName
