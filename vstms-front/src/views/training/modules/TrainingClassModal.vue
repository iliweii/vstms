<template>
  <a-drawer :title="title" width="60%" placement="right" :closable="false" @close="close" :visible="visible">
    <a-spin :spinning="confirmLoading">
      <a-form-model ref="form" :model="model" :rules="validatorRules">
        <a-row>
          <a-col>
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="no" label="编号">
              <a-input placeholder="编号自动生成" v-model="model.no" disabled />
            </a-form-model-item>
          </a-col>
          <a-col>
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="name" label="名称">
              <a-input placeholder="请输入名称" v-model="model.name" :disabled="disableSubmit" />
            </a-form-model-item>
          </a-col>
          <a-col :md="6">
            <a-form-model-item :labelCol="labelCol2" :wrapperCol="wrapperCol2" prop="period" label="课时">
              <a-input placeholder="请输入课时" suffix="时" v-model="model.period" :disabled="disableSubmit" />
            </a-form-model-item>
          </a-col>
          <a-col :md="6">
            <a-form-model-item :labelCol="labelCol2" :wrapperCol="wrapperCol2" prop="price" label="价格">
              <a-input
                placeholder="请输入价格"
                prefix="￥"
                suffix="RMB"
                v-model="model.price"
                :disabled="disableSubmit"
              />
            </a-form-model-item>
          </a-col>
          <a-col :md="6">
            <a-form-model-item :labelCol="labelCol2" :wrapperCol="wrapperCol2" prop="teacherNum" label="教师数量">
              <a-input placeholder="请输入教师数量" suffix="人" v-model="model.teacherNum" :disabled="disableSubmit" />
            </a-form-model-item>
          </a-col>
          <a-col :md="6">
            <a-form-model-item :labelCol="labelCol2" :wrapperCol="wrapperCol2" prop="studentNum" label="学生数量">
              <a-input placeholder="请输入学生数量" suffix="人" v-model="model.studentNum" :disabled="disableSubmit" />
            </a-form-model-item>
          </a-col>
          <a-col>
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="skillName" label="职业技能名称">
              <a-input placeholder="请输入职业技能" v-model="model.skillName" :disabled="disableSubmit" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="description" label="描述">
              <a-textarea
                v-model="model.description"
                placeholder="请输入描述"
                :autoSize="{ minRows: 5, maxRows: 10 }"
                :disabled="disableSubmit"
              />
            </a-form-model-item>
            <a-form-model-item v-if="model.id" :labelCol="labelCol" :wrapperCol="wrapperCol" label="附件">
              <j-file-upload
                ref="jFileUpload"
                :disabled="disableSubmit"
                businessType="training_class"
                :objectId="model.id"
                :labelColumn="{ span: 0 }"
                :wrapperColumn="{ span: 24 }"
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
import moment from 'moment'

export default {
  name: 'TrainingClassModal',
  data() {
    return {
      title: '操作',
      visible: false,
      model: {},
      labelCol: {
        xs: { span: 24 },
        sm: { span: 2 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 22 },
      },
      labelCol2: {
        xs: { span: 24 },
        sm: { span: 8 },
      },
      wrapperCol2: {
        xs: { span: 24 },
        sm: { span: 16 },
      },

      disableSubmit: false,
      confirmLoading: false,
      validatorRules: {},
      url: {
        add: '/training/trainingClass/add',
        edit: '/training/trainingClass/edit',
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
  z-index: 100;
}
</style>