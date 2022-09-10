function sendCode(params) {
    return $axios({
        'url': '/user/send',
        'method': 'get',
        params
    })
}

function loginApi(data) {
    return $axios({
      'url': '/user/login',
      'method': 'post',
      data
    })
  }

function loginoutApi() {
  return $axios({
    'url': '/user/loginout',
    'method': 'post',
  })
}

function userInfoApi() {
    return $axios({
        'url': '/user/info',
        'method': 'get',
    })
}


  