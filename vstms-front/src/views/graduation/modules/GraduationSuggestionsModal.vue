<template>
  <a-drawer :title="title" width="60%" placement="right" :closable="false" @close="close" :visible="visible">
    <a-spin :spinning="confirmLoading">
      <a-form-model ref="form" :model="model" :rules="validatorRules">
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="no" label="意见编号">
          <a-input placeholder="编号自动生成" v-model="model.no" disabled />
        </a-form-model-item>
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="obj" label="意见对象">
          <j-search-select-tag
            v-model="model.obj"
            :triggerChange="true"
            placeholder="请选择意见对象"
            dict="graduation_suggestions_obj"
            :disabled="disableSubmit"
          />
        </a-form-model-item>
        <a-form-model-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          prop="objValue"
          :label="getObjValueLable"
          v-if="getObjValueLable()"
        >
          <a-input placeholder="请输入" v-model="model.objValue" :disabled="disableSubmit" />
        </a-form-model-item>
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="content" label="意见内容">
          <j-editor v-model="model.content" autoSize :style="{ height: '500px' }" :disabled="disableSubmit" />
        </a-form-model-item>
      </a-form-model>

      <j-file-upload
        ref="jFileUpload"
        :disabled="disableSubmit"
        businessType="graduation_suggestions"
        :objectId="model.id"
        label="意见建议附件"
        text="点击添加意见建议附件"
        :labelColumn="{ span: 3 }"
        :wrapperColumn="{ span: 19 }"
        v-if="model.id"
      />
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
  name: 'GraduationSuggestionsModal',
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
        add: '/graduation/graduationSuggestions/add',
        edit: '/graduation/graduationSuggestions/edit',
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
    getObjValueLable() {
      if (this.model.obj == 'teacher') return '教师姓名'
      else if (this.model.obj == 'leader') return '班主任姓名'
      else if (this.model.obj == 'class') return '班级编号'
      else if (this.model.obj == 'course') return '课程名称'
      else return false
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