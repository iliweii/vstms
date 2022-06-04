<template>
  <a-card size="small" hoverable style="width: 100%; height: 100%">
    <a-row>
      <a-col :span="3" style="text-align: center">
        <a-avatar shape="square" :size="128" icon="user" :src="getAvatar(user.avatar)" />
      </a-col>
      <a-col :span="12">
        <a-row :gutter="20">
          <a-col :span="24">
            <span class="welcome-font">欢迎您，{{ user.realname }}</span>
          </a-col>
        </a-row>
        <a-row :gutter="5" style="margin-top: 20px">
          <a-col :span="12">
            <div class="one-tag">
              <a-tag color="blue"> 手机号 </a-tag>
              <span v-if="user.phone">{{ user.phone }}</span>
              <span v-else style="color: red">请设置用户手机号！</span>
            </div>
          </a-col>
          <a-col :span="12">
            <div class="one-tag">
              <a-tag color="blue"> 邮箱 </a-tag>
              <span v-if="user.email">{{ user.email }}</span>
              <span v-else style="color: red">请设置用户邮箱！</span>
            </div>
          </a-col>
          <a-col :span="12">
            <div class="one-tag">
              <a-tag color="blue"> 培训班 </a-tag>
              <j-ellipsis :value="className" :length="50"></j-ellipsis>
            </div>
          </a-col>
          <a-col :span="12">
            <div class="one-tag">
              <a-tag color="blue"> 角色 </a-tag>
              <j-ellipsis :value="roleName" :length="50"></j-ellipsis>
            </div>
          </a-col>
        </a-row>
      </a-col>
      <a-col
        :span="9"
        style="height: 120px; display: flex; align-content: center; justify-content: center; align-items: center"
      >
        <iframe
          allowtransparency="true"
          frameborder="0"
          width="400"
          height="96"
          scrolling="no"
          src="//tianqi.2345.com/plugin/widget/index.htm?s=1&z=1&t=0&v=0&d=3&bd=0&k=&f=&ltf=009944&htf=cc0000&q=1&e=0&a=1&c=54511&w=400&h=96&align=right"
        ></iframe>
      </a-col>
    </a-row>
  </a-card>
</template>

<script>
import Vue from 'vue'
import { USER_INFO, ENHANCE_PRE } from '@/store/mutation-types'
import { getFileAccessHttpUrl, getAction } from '@/api/manage'

export default {
  name: 'UserInfoWidget',
  data() {
    return {
      className: '',
      roleName: '',
    }
  },
  computed: {
    user() {
      return Vue.ls.get(USER_INFO)
    },
  },
  mounted() {
    this.init()
  },
  methods: {
    init() {
      this.initClass()
      this.initRole()
    },
    initClass() {
      getAction('/training/trainingClassStudent/queryClass', { username: this.user.username }).then((res) => {
        this.className = res.result
      })
    },
    initRole() {
      getAction('/sys/user/queryUserRoleName', { userid: this.user.id }).then((res) => {
        this.roleName = res.result
      })
    },
    getAvatar(avatar) {
      return getFileAccessHttpUrl(avatar)
    },
  },
}
</script>

<style scoped lang='less'>
.welcome-font {
  font-size: 24px !important;
}
iframe {
  pointer-events: none;
}
.one-tag {
  padding: 5px;
}
</style>
