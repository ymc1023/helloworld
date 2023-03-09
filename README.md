


#aws 
aws configure
aws sts get-caller-identity
aws eks describe-cluster --region us-east-1 --name {K8S 클러스터명}
aws eks update-kubeconfig --region us-east-1 --name {K8S 클러스터명}













#alb를 생성하기 위한 절차

curl -O https://raw.githubusercontent.com/kubernetes-sigs/aws-load-balancer-controller/v2.4.4/docs/install/iam_policy.json
vi iam_policy.json
aws iam create-policy --policy-name AWSLoadBalancerControllerIAMPolicy --policy-document file://iam_policy.json
aws iam list-open-id-connect-providers | grep $oidc_id | cut -d "/" -f4
aws iam list-open-id-connect-providers | grep $(aws eks describe-cluster --name {K8S 클러스터명} --query "cluster.identity.oidc.issuer" --output text | cut -d '/' -f 5) | cut -d "/" -f4
aws eks describe-cluster --name {K8S 클러스터명} --query "cluster.identity.oidc.issuer" --output text | cut -d '/' -f 5
cat >load-balancer-role-trust-policy.json <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Principal": {
                "Federated": "arn:aws:iam::{AWS accountID}:oidc-provider/oidc.eks.us-east-1.amazonaws.com/id/81D22A0190EBD3267D1DFE954D8620A4"
            },
            "Action": "sts:AssumeRoleWithWebIdentity",
            "Condition": {
                "StringEquals": {
                    "oidc.eks.region-code.amazonaws.com/id/81D22A0190EBD3267D1DFE954D8620A4:aud": "sts.amazonaws.com",
                    "oidc.eks.region-code.amazonaws.com/id/81D22A0190EBD3267D1DFE954D8620A4:sub": "system:serviceaccount:kube-system:aws-load-balancer-controller"
                }
            }
        }
    ]
}
EOF

aws iam create-role   --role-name AmazonEKSLoadBalancerControllerRole   --assume-role-policy-document file://"load-balancer-role-trust-policy.json"
aws iam attach-role-policy   --policy-arn arn:aws:iam::{AWS accountID}:policy/AWSLoadBalancerControllerIAMPolicy   --role-name AmazonEKSLoadBalancerControllerRole

cat >aws-load-balancer-controller-service-account.yaml <<EOF
apiVersion: v1
kind: ServiceAccount
metadata:
  labels:
    app.kubernetes.io/component: controller
    app.kubernetes.io/name: aws-load-balancer-controller
  name: aws-load-balancer-controller
  namespace: kube-system
  annotations:
    eks.amazonaws.com/role-arn: arn:aws:iam::{AWS accountID}:role/AmazonEKSLoadBalancerControllerRole
EOF

kubectl apply -f aws-load-balancer-controller-service-account.yaml
helm repo add eks https://aws.github.io/eks-charts
helm repo update
kubectl get deployment -n kube-system aws-load-balancer-controller
helm upgrade -i aws-load-balancer-controller eks/aws-load-balancer-controller --set region=us-east-1 --set vpcId=vpc-04eb08d6fdd0a9a18 --set clusterName={K8S 클러스터명} --set serviceAccount.create=false --set serviceAccount.name=aws-load-balancer-controller -n kube-system



