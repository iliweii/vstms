<template>
  <a-drawer :title="title" width="60%" placement="right" :closable="false" @close="close" :visible="visible">
    <a-spin :spinning="confirmLoading">
      <a-form-model ref="form" :model="model" :rules="validatorRules">
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="classNo" label="培训班编号">
          <a-input placeholder="培训班编号" v-model="model.classNo" disabled />
        </a-form-model-item>
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="username" label="用户名">
          <a-input placeholder="用户名" v-model="model.username" disabled />
        </a-form-model-item>
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="admissionDate" label="入学日期">
          <a-date-picker v-model="model.admissionDate" />
        </a-form-model-item>
      </a-form-model>

      <j-file-upload
        ref="jFileUpload"
        :disabled="model.status != '0'"
        businessType="student_registration"
        :objectId="model.id"
        label="报到附件"
        text="点击添加报到附件"
        :labelColumn="{ span: 3 }"
        :wrapperColumn="{ span: 19 }"
      />
    </a-spin>

    <div class="drawer-bootom-button">
      <a-popconfirm
        title="确定标记为已报到吗？标记后不可修改"
        @confirm="() => handleOk()"
        :disabled="model.status != '0'"
      >
        <a-button type="primary" icon="check" :disabled="model.status != '0'">标记为已报到</a-button>
      </a-popconfirm>

      <a-button type="primary" @click="handleCancel">取消</a-button>
    </div>
  </a-drawer>
</template>

<script>
import { httpAction } from '@/api/manage'
import moment from 'moment'

export default {
  name: 'StudentRegistrationModal',
  data() {
    return {
      title: '操作',
      visible: false,
      model: {},
      labelCol: {
        xs: { span: 24 },
        sm: { span: 3 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 19 },
      },

      confirmLoading: false,
      validatorRules: {},
      url: {
        add: '/training/trainingClassStudent/add',
        edit: '/training/trainingClassStudent/edit',
      },
    }
  },
  created() {},
  methods: {
    add() {
      //初始化默认值
      this.edit({})
    },
    edit(record) {
      // 若没有入学报到日期，且未报到，则显示为今天
      if (!record.admissionDate && record.status == '0') {
        record.admissionDate = this.getTime()
      }
      this.model = Object.assign({}, record)
      this.visible = true
    },
    close() {
      this.$emit('close')
      this.visible = false
      this.$refs.form.clearValidate()
    },
    handleOk() {
      const that = this
      // 触发表单验证
      this.$refs.form.validate((valid) => {
        if (valid) {
          that.confirmLoading = true
          let httpurl = this.url.edit
          let method = 'put'
          this.model.status = '1'
          httpAction(httpurl, this.model, method)
            .then((res) => {
              if (res.success) {
                that.$message.success(res.message)
                that.$emit('ok')
              } else {
                that.$message.warning(res.message)
              }
            })
            .finally(() => {
              that.confirmLoading = false
              that.close()
            })
        } else {
          return false
        }
      })
    },
    handleCancel() {
      this.close()
    },
    /**
     * 获取格式化时间
     * @returns 格式化时间YYYY-MM-DD
     */
    getTime() {
      var myDate = new Date()
      var myYear = myDate.getFullYear()
      var myMonth = myDate.getMonth() + 1
      var myToday = myDate.getDate()
      if (myMonth / 10) myMonth = '0' + myMonth
      return `${myYear}-${myMonth}-${myToday}`
    },
  },
}
</script>

<style lang="less" scoped>
/**Button按钮间距*/
.ant-btn {
  margin-left: 30px;
  margin-bottom: 30px;
  float: right;
}
/**抽屉按钮样式*/
.drawer-bootom-button {
  position: absolute;
  bottom: -8px;
  width: 100%;
  border-top: 1px solid #e8e8e8;
  padding: 10px 16px;
  text-align: right;
  left: 0;
  background: #fff;
  border-radius: 0 0 2px 2px;
  z-index: 100;
}
</style>