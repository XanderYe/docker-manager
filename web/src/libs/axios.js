//引入axios
import axios from 'axios'
import router from '../router/index.js'

axios.defaults.baseURL = baseUrl;
// 超时请求
axios.defaults.timeout = 10000;
// 携带cookie
axios.defaults.withCredentials = true;
// 响应拦截器即异常处理
axios.interceptors.response.use(response => {
  return response
}, err => {
  if (err && err.response) {
    requests.error(err.response.status);
  } else {
    err.message = "连接到服务器失败"
  }
  return Promise.reject(err.response);
});

const requests = {

  //get请求
  get(url, param) {
    return new Promise((resolve, reject) => {
      axios({
        method: 'get',
        url,
        params: param,
        headers: {
          "token": localStorage.getItem("token")
        }
      }).then(res => {
        this.error(res.data.code);
        resolve(res);
      }).catch((res) => {
        reject(res);
      })
    })
  },
  //post请求
  post(url, param) {
    return new Promise((resolve, reject) => {
      axios({
        method: 'post',
        url,
        data: param,
        headers: {
          "token": localStorage.getItem("token")
        }
      }).then(res => {
        this.error(res.data.code);
        resolve(res);
      }).catch((res) => {
        reject(res);
      })
    })
  },

  //post请求
  upload(url, param) {
    return new Promise((resolve, reject) => {
      axios({
        method: 'post',
        url,
        data: param,
        headers: {
          "content-type": "multipart/form-data",
          "token": localStorage.getItem("token")
        }
      }).then(res => {
        resolve(res);
      }).catch((res) => {
        reject(res);
      })
    })
  },

  getFiles(url, params, fileName) {
    fileName = fileName || "";
    return new Promise((resolve, reject) => {
      axios.get(url, {
        params: params,
        responseType: 'blob',
      }).then(res => {
        let url = window.URL.createObjectURL(res.data);
        let link = document.createElement('a');
        link.style.display = 'none';
        link.href = url;
        link.setAttribute('download', fileName);
        document.body.appendChild(link);
        link.click();
        resolve(res);
      }).catch(err => {
        reject(err)
      })
    })
  },

  error(status) {
    switch (status) {
      //请求没有权限状态码
      case 10001:
        router.replace({
          path: '/login'
        });
        break;
      //请求未找到
      case 404:
        router.replace({
          path: '/error-404'
        });
        break;
    }
  }


};
export default requests;
