minikube start 
kubectx
kubectl apply -f namespace.yaml // создаём новый нэймспейс используя файл
kubens проверяем активный нэймспейс
kubens packman меняем активный нэймспейс	
kubectl apply -f mongo-deployment.yaml *деплоим ПОД "монго" из файла
kubectl get pods *проверяем что создался ПОД "монго"
kubectl logs mongo-0 * логи ПОДа
kubectl apply -f packman-deployment.yaml
kubectl delete -f packman-deployment.yaml *удаляем ПОД
