## This is a simple spring boot todo app where users can create & retrieve tasks

# Deploying the todo spring boot microservice to AWS EKS
## Eksctl setup
### Eksctl CLI is used for creating & managing kubernetes clusters in AWS.
- Install Homebrew
- Add aws tap
``` 
brew tap aws/tap
```
- Install eksctl
```
brew install aws/tap/eksctl
```

## Kubectl setup
### Kubectl CLI is used to manage resources within kubernetes cluster.
- Check if kubectl is already installed on the device
```
kubectl version --client
```
### Install or update kubectl
```declarative
curl -O https://s3.us-west-2.amazonaws.com/amazon-eks/1.34.1/2025-09-19/bin/darwin/amd64/kubectl
```
### Create an AWS EKS cluster
```declarative
eksctl create cluster \
	--name todo-app-cluster \
	--nodegroup-name worknodes-1 \
	--node-type t3.medium \
	--nodes 2 \
	--nodes-min 1 \
	--nodes-max 3 \
	--managed \
	--version 1.33 \
	--region ${AWS_REGION}
```
	
### When creating an EKS cluster with eksctl, the utility creates:-
- A new Virtual Private Cloud (VPC).
- Three public and three private subnets across different Availability Zones. 
- Required security groups and IAM roles.
- Amazon EKS control plane and worker nodes.

Understanding the command flags:-
#### Cluster Configuration
- eksctl create cluster: Initiates the cluster creation process.
- –name todo-app-cluster: Assigns a unique identifier to your cluster.
- –version 1.33: Sets the Kubernetes version for your cluster.
- –region ${AWS_REGION}: Determines where AWS creates your cluster resources.
#### Node Group Settings
- –nodegroup-name worknodes-1: Names the group of worker nodes.
- –node-type t3.medium: Selects the EC2 instance type for worker nodes.
- –managed: Enables AWS to handle node group maintenance and updates.
#### Auto Scaling Configuration
- –nodes 2: Sets the initial number of worker nodes.
- –nodes-min 1: Defines the minimum nodes during scaling events.
- –nodes-max 3: Sets the maximum nodes allowed during high demand.

Note: While eksctl creates a new VPC by default, you can also configure it to use existing network resources.

Here is the sample output of create cluster command:-
```
[ℹ] eksctl version 0.215.0
[ℹ] using region us-west-2
[!] Amazon EKS will no longer publish EKS-optimized Amazon Linux 2 (AL2) AMIs after November 26th, 2025. Additionally, Kubernetes version 1.32 is the last version for which Amazon EKS will release AL2 AMIs. From version 1.33 onwards, Amazon EKS will continue to release AL2023 and Bottlerocket based AMIs. The default AMI family when creating clusters and nodegroups in Eksctl will be changed to AL2023 in the future.
[ℹ] setting availability zones to [us-west-2a us-west-2c us-west-2d]
[ℹ] subnets for us-west-2a - public:192.168.0.0/19 private:192.168.96.0/19
[ℹ] subnets for us-west-2c - public:192.168.32.0/19 private:192.168.128.0/19
[ℹ] subnets for us-west-2d - public:192.168.64.0/19 private:192.168.160.0/19
[ℹ] nodegroup "worknodes-1" will use "" [AmazonLinux2/1.29]
[!] Auto Mode will be enabled by default in an upcoming release of eksctl. This means managed node groups and managed networking add-ons will no longer be created by default. To maintain current behavior, explicitly set 'autoModeConfig.enabled: false' in your cluster configuration. Learn more: https://eksctl.io/usage/auto-mode/
[ℹ] using Kubernetes version 1.29
[ℹ] creating EKS cluster "todo-app-cluster" in "us-west-2" region with managed nodes
[ℹ] will create 2 separate CloudFormation stacks for cluster itself and the initial managed nodegroup
[ℹ] if you encounter any issues, check CloudFormation console or try 'eksctl utils describe-stacks --region=us-west-2 --cluster=todo-app-cluster'
[ℹ] Kubernetes API endpoint access will use default of {publicAccess=true, privateAccess=false} for cluster "todo-app-cluster" in "us-west-2"
[ℹ] CloudWatch logging will not be enabled for cluster "todo-app-cluster" in "us-west-2"
[ℹ] you can enable it with 'eksctl utils update-cluster-logging --enable-types={SPECIFY-YOUR-LOG-TYPES-HERE (e.g. all)} --region=us-west-2 --cluster=todo-app-cluster'
[ℹ] default addons coredns, metrics-server, vpc-cni, kube-proxy were not specified, will install them as EKS addons
[ℹ] 
2 sequential tasks: { create cluster control plane "todo-app-cluster",
  2 sequential sub-tasks: {
    2 sequential sub-tasks: {
      1 task: { create addons },
      wait for control plane to become ready,
    },
    create managed nodegroup "worknodes-1",
  }
}
[ℹ] building cluster stack "eksctl-todo-app-cluster-cluster"
[ℹ] deploying stack "eksctl-todo-app-cluster-cluster"
[ℹ] waiting for CloudFormation stack "eksctl-todo-app-cluster-cluster"
[ℹ] waiting for CloudFormation stack "eksctl-todo-app-cluster-cluster"
[ℹ] waiting for CloudFormation stack "eksctl-todo-app-cluster-cluster"
[ℹ] waiting for CloudFormation stack "eksctl-todo-app-cluster-cluster"
[ℹ] waiting for CloudFormation stack "eksctl-todo-app-cluster-cluster"
[ℹ] waiting for CloudFormation stack "eksctl-todo-app-cluster-cluster"
```

### Verify and configure the cluster
```declarative
- eksctl get cluster --region $AWS_REGION
- eksctl get nodegroup --cluster=todo-app-cluster --region $AWS_REGION
```

After verifying the cluster management capabilities of eksctl, the next step verifies how kubectl communicates with cluster resources.
```declarative
kubectl cluster-info

Kubernetes control plane is running at https://E52E57CC60DFD1C4E20212777374180F.gr7.us-west-2.eks.amazonaws.com
CoreDNS is running at https://E52E57CC60DFD1C4E20212777374180F.gr7.us-west-2.eks.amazonaws.com/api/v1/namespaces/kube-system/services/kube-dns:dns/proxy
```

```declarative
kubectl get nodes
```

### Deploy the containerized todo-app application

To view existing deployments, services, and pods in your cluster, run the following command:
```declarative
kubectl get deployment,service,pod --all-namespaces
```

Next, create a kubernetes secret for the DB env params to be used at runtime for database connection.
```yaml
apiVersion: v1
kind: Secret
metadata:
 name: todo-db-secret
type: Opaque
stringData:
 APP_TOKEN: "XXXXXXX"
 DATABASE_ID: "XXXXXXX"
 DATABASE_REGION: "XXXXXXXX"
```
Next, create a deployment manifest that defines how Kubernetes should deploy the application. Deployment manifest contains the docker image location & secret params referenced as env params

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: todo-app-deployment
  labels:
    app: todo-app
spec:
  replicas: 2
  selector:
    matchLabels:
      app: todo-app
  template:
    metadata:
      labels:
        app: todo-app
    spec:
      imagePullSecrets:
        - name: aws-ecr-cred # create using `kubectl create secret docker-registry` as shown earlier
      containers:
        - name: todo-app
          image: XXXXXXXXX.dkr.ecr.us-east-1.amazonaws.com/todo-app:latest
          ports:
            - containerPort: 8080
          env:
            - name: APP_TOKEN
              valueFrom:
                secretKeyRef:
                  name: todo-db-secret
                  key: APP_TOKEN
            - name: DATABASE_ID
              valueFrom:
                secretKeyRef:
                  name: todo-db-secret
                  key: DATABASE_ID
            - name: DATABASE_REGION
              valueFrom:
                secretKeyRef:
                  name: todo-db-secret
                  key: DATABASE_REGION
```

While the pods are now running, they aren’t yet accessible from outside the cluster. To enable external access, we need to create a Kubernetes service.
```yaml
apiVersion: v1
kind: Service
metadata:
  name: todo-app-service
  labels:
    app: todo-app
spec:
  type: LoadBalancer
  selector:
    app: todo-app
  ports:
    - protocol: TCP
      port: 80 # external load balancer port
      targetPort: 8080 # container port
```
This configuration provisions a load balancer and establishes a network endpoint that routes incoming HTTP traffic to the pods, making the application accessible from outside of the cluster.

To create the deployment, service & secrets in the cluster, run the following commands:-
```declarative
Kubectl apply -f todo-db-secret.yaml
Kubectl apply -f todo-app-deployment.yaml
Kubectl apply -f todo-app-service.yaml
```
Once the artifacts created successfully, run the following commands to get the deployment/pod status

```declarative
kubectl get deployment,pods

NAME                 READY  UP-TO-DATE  AVAILABLE  AGE
deployment.apps/todo-app-deployment  2/2   2      2      11s

NAME                   READY  STATUS  RESTARTS  AGE
pod/todo-app-deployment-6c66c454f-4ll4v  1/1   Running  0     11s
pod/todo-app-deployment-6c66c454f-ltwkw  1/1   Running  0     11s
```
copy the DNS name from load balancer and try making http requests and you should see them returning results.
