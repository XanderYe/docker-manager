<template>
  <div class="login">
    <div class="logo"></div>
    <div class="login-con">
      <Card>
        <p slot="title">
          <span>{{title}}</span>
        </p>
        <div name="login">
          <Form class="form" ref="loginForm" :rules="loginRules" :model="loginForm" v-show="tab === 'login'">
            <FormItem prop="username" >
              <Input type="text" v-model="loginForm.username" placeholder="请输入用户名">
                <Icon type="ios-person-outline" slot="prepend"></Icon>
              </Input>
            </FormItem>
            <FormItem prop="password">
              <Input type="password" v-model="loginForm.password" placeholder="请输入密码">
                <Icon type="ios-lock-outline" slot="prepend"></Icon>
              </Input>
            </FormItem>
            <FormItem>
              <Button type="primary" :loading="loginLoading" :long="true" @click="login">登录</Button>
            </FormItem>
          </Form>
        </div>
      </Card>
    </div>
  </div>
</template>

<script>
  export default {
    name: "Login",
    data() {
      return {
        title: "欢迎登录",
        tab: "login",
        // 登录信息
        loginForm: {
          username: "",
          password: "",
        },
        // 登录验证规则
        loginRules: {
          username: [
            { required: true, message: '用户名不能为空', trigger: 'blur' }
          ],
          password: [
            { required: true, message: '密码不能为空', trigger: 'blur' }
          ]
        },
        // 登录加载
        loginLoading: false,
      }
    },
    methods: {
      login() {
        this.$refs["loginForm"].validate((valid) => {
          if (valid) {
            this.loginLoading = true;
            let form = new FormData();
            form.append("username", this.loginForm.username);
            form.append("password", this.loginForm.password);
            this.$requests.post("/login", form).then(res => {
              if (res.data.code === 0) {
                this.$Message.success("登录成功");
                localStorage.setItem("token", res.data.data);
                setTimeout(() => {
                  this.$router.push({name: "index"});
                }, 1000)
              } else {
                this.$Message.error(res.data.msg);
              }
              setTimeout(() => {
                this.loginLoading = false;
              }, 500)
            })
          }
        })
      },
    }
  }
</script>

<style lang="less">
  .login {
    width: 100%;
    height: 100%;
    background-color: #E7E7E7;

    .logo {
      position: absolute;
      left: 50%;
      top: 5%;
      width: 200px;
      height: 200px;
      margin-left: -100px;
      background-image: url("../assets/docker-logo.png");
      background-size: cover;
    }

    .login-con {
      position: absolute;
      left: 50%;
      top: 50%;
      -webkit-transform: translateY(-60%);
      transform: translateY(-60%);
      width: 400px;
      margin-left: -200px;

      .form {
        padding: 10px 0;

        .ivu-form-item {
          width: 100%;
        }
      }

      .ivu-tabs-bar {
        display: none;
      }

      a {
        float: right;
      }
    }
  }
</style>
