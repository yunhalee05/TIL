import grpc from 'k6/net/grpc';
import { check, sleep } from 'k6';

const client = new grpc.Client();
client.load(['/protos'], 'schema.proto');

export default () => {
  client.connect('grpc-service-helm-chart.test.svc.cluster.local:9090', { plaintext: true });

  const res = client.invoke('com.yunhalee.performance_test.BookAuthorService/getAuthor', {
    "author_id": 13,
    "book_id": 5185,
    "first_name": "john",
    "gender": "female",
    "last_name": "lee"
  });

  check(res, {
    'status is OK': (r) => r && r.status === grpc.StatusOK,
  });

  client.close();
  sleep(1);
};
