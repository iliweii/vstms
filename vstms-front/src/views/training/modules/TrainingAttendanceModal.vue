<template>
  <a-drawer :title="title" :width="800" placement="right" :closable="false" @close="close" :visible="visible">
    <a-spin :spinning="confirmLoading">
      <a-form-model ref="form" :model="model" :rules="validatorRules">
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="classNo" label="培训班编号">
          <a-input placeholder="请输入培训班编号" v-model="model.classNo" disabled />
        </a-form-model-item>
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="username" label="学员用户名">
          <a-input placeholder="请输入学员用户名" v-model="model.username" disabled />
        </a-form-model-item>
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="atdDate" label="考勤日期">
          <j-date
            placeholder="请选择考勤日期"
            style="width: 100%"
            v-model="model.atdDate"
            :showTime="true"
            dateFormat="YYYY-MM-DD"
            :disabled="true"
          />
        </a-form-model-item>
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="atdStatus" label="考勤状态">
          <j-search-select-tag
            v-model="model.atdStatus"
            :triggerChange="true"
            placeholder="请选择考勤状态"
            dict="training_attendance_status"
            :disabled="disableSubmit"
          />
        </a-form-model-item>
      </a-form-model>
    </a-spin>

    <div class="drawer-bootom-button">
      <a-button type="primary" @click="handleOk" :disabled="disableSubmit">确定</a-button>
      <a-button type="primary" @click="handleCancel">取消</a-button>
    </div>
  </a-drawer>
</template>

<script>
import { httpAction } from '@/api/manage'
import moment from 'moment'

export default {
  name: 'TrainingAttendanceModal',
  data() {
    return {
      title: '操作',
      visible: false,
      disableSubmit: false,
      model: {},
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },

      confirmLoading: false,
      validatorRules: {},
      url: {
        add: '/training/trainingAttendance/add',
        edit: '/training/trainingAttendance/edit',
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
          let httpurl = ''
          let method = ''
          if (!this.model.id) {
            httpurl += this.url.add
            method = 'post'
          } else {
            httpurl += this.url.edit
            method = 'put'
          }
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
}
</style>