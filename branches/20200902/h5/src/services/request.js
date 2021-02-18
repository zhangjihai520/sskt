import axios from 'axios';
import store from '../store';

let $axios = axios.create();
export default function request(method, url, data) {
  var headers = {
    CurUserSystem: store.state.CurUserSystem
  };
  return new Promise((reslove, reject) => {
    $axios({
      method,
      url,
      data: data,
      headers: headers
    }).then(res => {
      let body = res.data
      if (res.status == 200 || res.code == 201) {
        if (body.ResultType != 1) {
          console.log("请求异常")
        }
        reslove(body.ReturnEntity);
      } else {
        console.log('请求异常！');
        reject(body);
      }
    }).catch(err => {
      console.log('请求异常！');
      reject(err);
    });
  })
}
