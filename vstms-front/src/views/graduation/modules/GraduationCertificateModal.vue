<template>
  <a-drawer :title="title" width="50%" placement="right" :closable="false" @close="close" :visible="visible">
    <a-divider orientation="left"> {{ model.student_dictText }}的学业信息 </a-divider>

    <a-row :gutter="24" style="padding: 20px 50px">
      <a-col :span="8">
        <a-statistic title="已参加课程数/课程数" :value="infoObject.examNum">
          <template #suffix>
            <span> / {{ infoObject.courseNum }}</span>
          </template>
        </a-statistic>
      </a-col>
      <a-col :span="8">
        <a-statistic title="平均课程成绩" :value="infoObject.avgScore">
          <template #suffix>
            <span> / 100分</span>
          </template>
        </a-statistic>
      </a-col>
      <a-col :span="8">
        <a-statistic title="课程成绩总和" :value="infoObject.sumScore">
          <template #suffix>
            <span> 分</span>
          </template>
        </a-statistic>
      </a-col>
      <a-col :span="24" v-if="infoObject.score && infoObject.score.length > 0">
        <a-table :columns="columns" :data-source="infoObject.score" size="small" :pagination="false" rowKey="id">
          <template slot="title"> 成绩详细信息 </template>
        </a-table>
      </a-col>
    </a-row>

    <a-divider orientation="left"> 证书基本信息 </a-divider>

    <a-spin :spinning="confirmLoading">
      <a-form-model ref="form" :model="model" :rules="validatorRules">
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="no" label="证书编号">
          <a-input placeholder="自动生成证书编号" v-model="model.no" disabled />
        </a-form-model-item>
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="classNo" label="培训班编号">
          <a-input placeholder="请输入培训班编号" v-model="model.classNo" disabled />
        </a-form-model-item>
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="student_dictText" label="学员姓名">
          <a-input placeholder="请输入学员姓名" v-model="model.student_dictText" disabled />
        </a-form-model-item>
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="studentId" label="学员身份证号" required>
          <a-input placeholder="请输入学员身份证号" v-model="model.studentId" :disabled="disableSubmit" />
        </a-form-model-item>
      </a-form-model>
    </a-spin>

    <div class="drawer-bootom-button">
      <a-button type="primary" @click="handleOk" :disabled="disableSubmit" icon="check">完成并生成合格证书</a-button>
      <a-button type="primary" @click="handleCancel">取消</a-button>
    </div>
  </a-drawer>
</template>

<script>
import { httpAction } from '@/api/manage'
import { deleteAction, getAction, downFile, getFileAccessHttpUrl } from '@/api/manage'
import moment from 'moment'

export default {
  name: 'GraduationCertificateModal',
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
      infoObject: {},

      validatorRules: {
        studentId: [
          { required: true, message: '请输入学员身份证号', trigger: 'blur' },
          // { validator: this.validatorStudentId, trigger: 'blur' },
          { pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/, message: '身份号格式不正确', trigger: 'blur' },
        ],
      },
      url: {
        add: '/graduation/graduationCertificate/add',
        edit: '/graduation/graduationCertificate/edit',
        queryByStudent: '/graduation/graduationCertificate/queryByStudent',
      },
      columns: [
        {
          title: '课程名称',
          dataIndex: 'courseName',
        },
        {
          title: '成绩',
          dataIndex: 'score',
        },
      ],
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
      this.getInfoByStudent()
    },
    async getInfoByStudent() {
      if (!this.model.classNo) {
        this.$message.error('请先选择培训班')
        return
      }
      const { result } = await getAction(this.url.queryByStudent, {
        classNo: this.model.classNo,
        student: this.model.student,
      })
      this.infoObject = result
    },
    close() {
      this.$emit('close')
      this.visible = false
      this.infoObject = {}
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

/deep/ .ant-statistic-content .ant-statistic-content-value-int,
/deep/.ant-statistic-content {
  color: rgba(0, 0, 0, 0.85);
  font-size: 24px !important;
  font-family: -apple-system, BlinkMacSystemFont, Segoe UI, PingFang SC, Hiragino Sans GB, Microsoft YaHei,
    Helvetica Neue, Helvetica, Arial, sans-serif, Apple Color Emoji, Segoe UI Emoji, Segoe UI Symbol;
}
/deep/ .ant-statistic-content-suffix {
  margin-left: 4px;
  font-size: 16px !important;
}
</style>