<template>
  <a-drawer :title="title" width="50%" placement="right" :closable="false" @close="close" :visible="visible" style="margin-bottom: 50px;">
    <a-spin :spinning="confirmLoading">
      <a-card title="教学视导记录表" id="Print">
        <a-card-grid style="width: 25%" :hoverable="false"> 视导人员 </a-card-grid>
        <a-card-grid style="width: 25%" :hoverable="false"> {{ model.createBy_dictText }} </a-card-grid>
        <a-card-grid style="width: 25%" :hoverable="false"> 视导日期 </a-card-grid>
        <a-card-grid style="width: 25%" :hoverable="false"> {{ model.svTime }} </a-card-grid>
        <a-card-grid style="width: 25%" :hoverable="false"> 培训班编号 </a-card-grid>
        <a-card-grid style="width: 75%" :hoverable="false"> {{ model.classNo }} </a-card-grid>
        <a-card-grid style="width: 25%" :hoverable="false"> 基本情况 </a-card-grid>
        <a-card-grid style="width: 75%" :hoverable="false">
          <pre class="pre">{{ model.situation }}</pre>
        </a-card-grid>
        <a-card-grid style="width: 25%" :hoverable="false"> 值得肯定的成绩 </a-card-grid>
        <a-card-grid style="width: 75%" :hoverable="false">
          <pre class="pre"> {{ model.excellent }} </pre>
        </a-card-grid>
        <a-card-grid style="width: 25%" :hoverable="false"> 存在的问题和不足 </a-card-grid>
        <a-card-grid style="width: 75%" :hoverable="false">
          <pre class="pre">{{ model.problems }}</pre>
        </a-card-grid>
        <a-card-grid style="width: 25%" :hoverable="false"> 改进的意见或建议 </a-card-grid>
        <a-card-grid style="width: 75%" :hoverable="false">
          <pre class="pre"> {{ model.suggestions }} </pre>
        </a-card-grid>
      </a-card>
    </a-spin>

    <div class="drawer-bootom-button">
      <a-button type="primary" v-print="'#Print'" icon="printer">打印</a-button>
      <a-button type="primary" @click="handleCancel">取消</a-button>
    </div>
  </a-drawer>
</template>

<script>
import { httpAction } from '@/api/manage'
import { mapGetters } from 'vuex'
import moment from 'moment'

export default {
  name: 'TrackingSupervisionPrint',
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

.pre {
  max-width: 100%;
  // 自动换行
  white-space: pre-wrap;
  word-wrap: break-word;
}

/deep/ .ant-card-grid {
  color: #000 !important;
  font-size: 16px;
  font-family: '黑体';
}
/deep/ .ant-card-head-title {
  color: #000 !important;
  font-size: 18px;
  font-weight: 700;
}
</style>