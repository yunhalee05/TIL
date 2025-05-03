# 성능테스트 하기 

### 1) grafana-influxdb 파드 배포 

##### 설정 값을 이용해서 두가지 서비스만 배포 
```bash
  helm install grafana-influx ./performance --namespace test \
  --set components.grafanaInfluxdb=true \
  --set components.k6=false
  # 파드 배포 성공 확인 
  ## grafana-influxdb                          2/2     Running   0          51m
  kubectl get pod -n test      
  kubectl get svc grafana-influxdb -n test
  
  # 포트 포워딩으로 grafana 접속
  ## localhost:3000으로 접속 가능  
  kubectl port-forward svc/grafana-influxdb 3000:3000 -n test   
```
##### influxdb에 k6 데이터 베이스 추가 
```bash
  # influxdb 포트 포워딩
  kubectl port-forward svc/grafana-influxdb 8086:8086 -n test   
 
  # k6 데이터 베이스 생성  
  ## HTTP/1.1 200 OK
  curl -i -XPOST http://localhost:8086/query --data-urlencode "q=CREATE DATABASE k6"
  
  # 데이터 베이스 생성 확인  
  ## k6 생성됨 확인 - {"results":[{"statement_id":0,"series":[{"name":"databases","columns":["name"],"values":[["k6"],["_internal"]]}]}]}
  curl -G http://localhost:8086/query \                                             
  --data-urlencode "q=SHOW DATABASES"
 
  # k6 데이터 베이스 데이터 확인 
  ## {"results":[{"statement_id":0}]}
  curl -G http://localhost:8086/query \
  --data-urlencode "db=k6" \
  --data-urlencode "q=SHOW MEASUREMENTS"
```

### 2) k6 파드 배포 
```bash
  # k6 파드 배포 
  helm install k6 ./performance --namespace test \
  --set components.grafanaInfluxdb=false \
  --set components.k6=true

  # 파드 배포 성공 확인 
  #  NAME                                      READY   STATUS      RESTARTS   AGE
  #grafana-influxdb                          2/2     Running     0          78m
  #grpc-service-helm-chart-d568bdc99-7lvnp   0/1     Running     0          7h17m
  #k6-nx8qf                                  0/1     Completed   0          3m4s
  kubectl get pod -n test      
 
  # Job의 재실행이 필요한 경우 (job 이름이 같기 떄문에) 
  kubectl delete job k6 -n test                   
  helm upgrade k6 ./performance --namespace test \
  --set components.grafanaInfluxdb=false \
  --set components.k6=true
  
  # 파드에서 서비스 파드 접근 가능 여부 테스트 
  kubectl exec -it k6-nnns8   -n test -- curl -i http://grafana-influxdb:8086/ping
```

##### 참고) 파드 모니터링 
```bash
  kubectl logs -f k6-pdfrl   -n test     
  kubectl describe pod k6-pdfrl -n test       
  kubectl logs job/k6 -n test          
  kubectl logs job/k6 -n test | tail -n 50 
```
``