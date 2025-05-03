# 로컬 도커 이미지 빌드, 도커 hub를 활용한 helm 배포 

### 1) 로컬 이미지 빌드 테스트 

##### 도커 이미지 빌드
```bash
  docker build -t test/grpc-service:latest .
```
##### kubernetes config가 없을 경우
```bash
  # kubernetes config 없음 확인 
  kubectl config current-context
  kubectl config get-contexts
  
  # minikube 설치 
  brew install minikube 
  
  # minikube 시작
  minikube start  
  
  # config 확인 
  kubectl config current-context     # minikube
```
##### 도커 이미지 실행
```bash
  docker run -p 9090:9090 yunhalee/grpc-service:latest
```
##### 도커 이미지 실행 확인
```bash
  # grpc ui로 동작 확인
  grpcui -plaintext localhost:9090
```


### 2) 도커 hub 이미지 + helm 배포 
##### 도커 hub에 로그인
```bash
  # docker hub에 로그인 
  docker login -u <docker id>
```

#### 도커 이미지 빌드 (이전 태그의 명을 도커 허브의 repository 명과 동일하게 설정)
```bash
  # docker hub의 이미지 명과 태그를 동일하게 지정 
   docker tag yunhalee/grpc-service:latest ohhh1232/test:latest
  # 이미지 확인 
  docker images
```

#### 도커 이미지 푸시
```bash
  # docker hub에 푸시 (이미지 명 레파지토리명을 동일하게 푸시) 
  docker push ohhh1232/test 
```

#### helm 배포 
```bash
  # 이전에 잘못 배포된 부분이 있다면 uninstall 
  helm uninstall grpc-service -n test      
  # docker hub의 이미지 명과 태그를 동일하게 지정 
  helm install grpc-service ./helm-chart --namespace test --create-namespace
```
#### 배포된 파드 호출 테스트 
```bash
  # 파드 상태 확인  
  kubectl get pods -n test
  kubectl describe pod grpc-service-helm-chart-d568bdc99-7lvnp -n test 

  # 파드 포트포워딩 
  kubectl port-forward svc/grpc-service 9090:9090 -n test
 
  # 포워딩된 파드 로컬에서 호출 확인 
  grpcui -plaintext localhost:9090
```