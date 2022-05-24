<template>
  <a-drawer :title="title" :width="800" placement="right" :closable="false" @close="close" :visible="visible">
    <a-spin :spinning="confirmLoading">
      <a-form-model ref="form" :model="model" :rules="validatorRules">
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="classNo" label="培训班编号">
          <a-input placeholder="培训班编号" v-model="model.classNo" disabled />
        </a-form-model-item>
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="day" label="星期" required>
          <j-search-select-tag
            v-model="model.day"
            :triggerChange="true"
            placeholder="请选择星期"
            dict="week"
            :disabled="disableSubmit"
          />
        </a-form-model-item>
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="several" label="节次" required>
          <j-search-select-tag
            v-model="model.several"
            :triggerChange="true"
            placeholder="请选择节次"
            dict="class_schedule_several"
            :disabled="disableSubmit"
          />
        </a-form-model-item>
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="courseName" label="课程名称" required>
          <a-input placeholder="请输入课程名称" v-model="model.courseName" :disabled="disableSubmit" />
        </a-form-model-item>
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="teacher" label="教师">
          <j-search-select-tag
            style="width: 100%"
            v-model="model.teacher"
            placeholder="请选择教师"
            :dictOptions="teacherDictOptions"
            :disabled="disableSubmit"
          />
        </a-form-model-item>
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remark" label="备注">
          <a-textarea
            v-model="model.remark"
            placeholder="请输入备注"
            :autoSize="{ minRows: 3, maxRows: 5 }"
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
import { getAction, httpAction } from '@/api/manage'
import moment from 'moment'

export default {
  name: 'TrainingClassScheduleModal',
  data() {
    return {
      title: '操作',
      visible: false,
      // 数据
      teacherDictOptions: [], // 教师下拉框list
      model: {},
      labelCol: {
        xs: { span: 24 },
        sm: { span: 3 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 19 },
      },

      disableSubmit: false,
      confirmLoading: false,
      validatorRules: {},
      url: {
        add: '/training/trainingClassSchedule/add',
        edit: '/training/trainingClassSchedule/edit',
        teacherList: '/training/trainingClassStudent/userList', // 教师列表
      },
    }
  },
  created() {
    this.getTeacherList()
  },
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
    /**
     * 异步获取教师list，并赋值到下拉选择列表teacherDictOptions中
     */
    async getTeacherList() {
      const { result } = await getAction(this.url.teacherList, {
        size: '-1',
        type: 'teacher',
        classNo: this.model.classNo,
      })
      this.teacherDictOptions = result.records.map((e) => {
        return {
          text: e.realname,
          value: e.username,
        }
      })
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