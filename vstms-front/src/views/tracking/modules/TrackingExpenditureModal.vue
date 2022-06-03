<template>
  <a-drawer :title="title" width="60%" placement="right" :closable="false" @close="close" :visible="visible">
    <a-spin :spinning="confirmLoading">
      <a-form-model ref="form" :model="model" :rules="validatorRules">
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="classNo" label="培训班编号" required>
          <a-input placeholder="请输入培训班编号" v-model="model.classNo" disabled />
        </a-form-model-item>
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="type" label="支出类型" required>
          <j-search-select-tag
            ref="selectTag"
            v-model="model.type"
            :triggerChange="true"
            placeholder="请选择支出类型"
            dict="tracking_expenditure_type"
            :disabled="disableSubmit"
          />
        </a-form-model-item>
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="costDate" label="支出日期" required>
          <j-date
            placeholder="请选择支出日期"
            v-model="model.costDate"
            :show-time="true"
            date-format="YYYY-MM-DD"
            style="width: 100%"
            :disabled="disableSubmit"
          />
        </a-form-model-item>
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="expend" label="支出金额(元)" required>
          <a-input-number
            placeholder="请输入支出金额(元)"
            v-model="model.expend"
            :min="0"
            :disabled="disableSubmit"
            :step="0.05"
            style="width: 100%"
          />
        </a-form-model-item>
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="project" label="支出项目" required>
          <a-input placeholder="请输入支出项目" v-model="model.project" :disabled="disableSubmit" />
        </a-form-model-item>
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remark" label="备注">
          <a-textarea
            placeholder="请输入备注"
            v-model="model.remark"
            :disabled="disableSubmit"
            :autosize="{ minRows: 3, maxRows: 5 }"
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
  name: 'TrackingExpenditureModal',
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
        add: '/tracking/trackingExpenditure/add',
        edit: '/tracking/trackingExpenditure/edit',
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