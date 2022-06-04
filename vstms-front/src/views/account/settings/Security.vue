<template>
  <a-list itemLayout="horizontal" :dataSource="data">
    <a-list-item slot="renderItem" slot-scope="item, index" :key="index">
      <a-list-item-meta>
        <a slot="title">{{ item.title }}</a>
        <span slot="description">
          <span class="security-list-description">{{ item.description }}</span>
          <span v-if="item.value"> : </span>
          <span class="security-list-value">{{ item.value }}</span>
        </span>
      </a-list-item-meta>
      <template v-if="item.actions">
        <a slot="actions" @click="item.actions.callback">{{ item.actions.title }}</a>
      </template>
    </a-list-item>

    <user-password ref="userPassword"></user-password>
    <a-modal v-model="visible" title="修改" @ok="handleOk" @cancel="handleCancel">
      <span><span style="color: red">*</span>{{ modelType == 'p' ? '手机号' : '邮箱' }}</span>
      <a-input placeholder="请输入" v-model="modelValue" />
      <span>修改成功后需重新登录</span>
    </a-modal>
  </a-list>
</template>

<script>
import UserPassword from '@/components/tools/UserPassword'
import { mapActions, mapGetters } from 'vuex'
import { putAction } from '@/api/manage'

export default {
  data() {
    return {
      visible: false,
      modelValue: '',
      modelType: '', // 'p' 'e'
      url: {
        edit: '/sys/user/edit',
      },

      data: [
        {
          title: '账户密码',
          description: '当前密码强度',
          value: '强',
          actions: {
            title: '修改',
            callback: () => {
              let username = this.userInfo().username
              this.$refs.userPassword.show(username)
            },
          },
        },
        {
          title: '密保手机',
          description: '已绑定手机',
          value: this.userInfo().phone,
          actions: {
            title: '修改',
            callback: () => {
              this.visible = true
              this.modelType = 'p'
              this.modelValue = this.userInfo().phone
            },
          },
        },
        {
          title: '安全邮箱',
          description: '已绑定邮箱',
          value: this.userInfo().email,
          actions: {
            title: '修改',
            callback: () => {
              this.visible = true
              this.modelType = 'e'
              this.modelValue = this.userInfo().email
            },
          },
        },
      ],
    }
  },
  components: {
    UserPassword,
  },
  methods: {
    ...mapActions(['Logout']),
    ...mapGetters(['nickname', 'avatar', 'userInfo']),
    handleOk() {
      const that = this
      let param = { id: this.userInfo().id }
      if (this.modelType === 'p') {
        param.phone = this.modelValue
      } else if (this.modelType === 'e') {
        param.email = this.modelValue
      }
      putAction(this.url.edit, param)
        .then((res) => {
          if (res.success) {
            this.$message.success('修改成功')
          } else {
            this.$message.error(res.message)
          }
        })
        .finally(() => {
          this.visible = false
          this.modelValue = ''
          this.modelType = ''
          that.Logout({}).then(() => {
            that.$router.push({ path: '/user/login' })
            window.location.reload()
          })
        })
    },
    handleCancel() {
      this.visible = false
      this.modelValue = ''
      this.modelType = ''
    },
  },
}
</script>

<style scoped>
</style>