<template>
  <a-drawer :title="title" width="70%" placement="right" :closable="false" @close="close" :visible="visible">
    <a-spin :spinning="confirmLoading">
      <a-form-model ref="form" :model="model" :rules="validatorRules">
        <a-row>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="classNo" label="培训班编号" required>
              <a-input placeholder="请输入培训班编号" v-model="model.classNo" disabled />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 16 }"
              prop="svTime"
              label="视导日期"
              required
            >
              <j-date
                placeholder="请选择视导日期"
                v-model="model.svTime"
                :show-time="true"
                date-format="YYYY-MM-DD"
                style="width: 100%"
                :disabled="disableSubmit"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item :labelCol="{ span: 4 }" :wrapperCol="{ span: 16 }" prop="svTime" label="视导人" required>
              <a-input placeholder="请输入视导人" v-model="model.createBy_dictText" disabled />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="situation" label="基本情况" required>
              <a-textarea
                placeholder="请输入基本情况"
                :rows="5"
                :max="450"
                v-model="model.situation"
                :disabled="disableSubmit"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="excellent"
              label="值得肯定的成绩"
              required
            >
              <a-textarea
                placeholder="请输入值得肯定的成绩"
                :rows="5"
                :max="450"
                v-model="model.excellent"
                :disabled="disableSubmit"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="problems"
              label="存在的问题和不足"
              required
            >
              <a-textarea
                placeholder="请输入存在的问题和不足"
                :rows="5"
                :max="450"
                v-model="model.problems"
                :disabled="disableSubmit"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="suggestions"
              label="改进的意见或建议"
              required
            >
              <a-textarea
                placeholder="请输入改进的意见或建议"
                :rows="5"
                :max="450"
                v-model="model.suggestions"
                :disabled="disableSubmit"
              />
            </a-form-model-item>
          </a-col>
        </a-row>
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
import { mapGetters } from 'vuex'
import moment from 'moment'

export default {
  name: 'TrackingSupervisionModal',
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
      disableSubmit: false,
      validatorRules: {},
      url: {
        add: '/tracking/trackingSupervision/add',
        edit: '/tracking/trackingSupervision/edit',
      },
    }
  },
  created() {},
  methods: {
    ...mapGetters(['userInfo']),
    add() {
      //初始化默认值
      this.edit({})
    },
    edit(record) {
      this.model = Object.assign({}, record)

      if (!record.createBy_dictText) {
        this.model.createBy_dictText = this.userInfo().realname
      }

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