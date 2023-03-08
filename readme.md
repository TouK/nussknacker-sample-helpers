#### Build designer image
```bash
./gradlew buildDesignerImage
```

#### Run NU in embedded mode (Request-Response) for manual testing
```bash
docker run -it -p 8080:8080 -p 8181:8181 -e DEFAULT_SCENARIO_TYPE=request-response-embedded nussknacker-sample-helper:latest
```

#### Notes
* Nu will start on localhost:8080 (admin/admin) with local hsql db 
* Endpoints of deployed scenarios will be available under http://localhost:8181/scenario/<scenario_name_or_slug_if_defined>
