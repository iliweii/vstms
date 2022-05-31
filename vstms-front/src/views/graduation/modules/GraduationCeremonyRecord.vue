<template>
  <a-card :bordered="false">
    <a-spin :spinning="false">
      <a-form-model ref="form" :model="model" :rules="validatorRules">
        <a-row>
          <a-col :md="12">
            <a-form-model-item :labelCol="{ span: 4 }" :wrapperCol="{ span: 18 }" prop="realDate" label="实际举行日期">
              <a-date-picker v-model="model.realDate" :disabled="disabled" :style="{ width: '100%' }" />
            </a-form-model-item>
          </a-col>
          <a-col :md="12">
            <a-form-model-item :labelCol="{ span: 4 }" :wrapperCol="{ span: 16 }" prop="realNum" label="实际参与人数">
              <a-input v-model="model.realNum" suffix="人" :disabled="disabled" />
            </a-form-model-item>
          </a-col>
          <a-col :md="24">
            <a-form-model-item :labelCol="{ span: 2 }" :wrapperCol="{ span: 20 }" prop="remark" label="备注">
              <a-textarea
                v-model="model.remark"
                placeholder="请输入备注"
                :autoSize="{ minRows: 5, maxRows: 10 }"
                :disabled="disabled"
              />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>

      <!-- 附件 -->
      <j-file-upload
        ref="jFileUpload"
        :disabled="disabled"
        businessType="graduation_ceremony_record"
        :objectId="object.classNo"
        label="记录"
        text="点击添加结业典礼记录"
        :labelColumn="{ span: 2 }"
        :wrapperColumn="{ span: 20 }"
      />
    </a-spin>

    <!-- 完成按钮 -->
    <div>
      <a-button @click="() => handleSubmit(object)" type="primary" :disabled="disabled" icon="check"
        >完成结业典礼记录上传</a-button
      >
      <div class="ant-form-extra" :style="{ fontSize: '12px' }">完成后不可撤销</div>
    </div>
  </a-card>
</template>

<script>
import { putAction } from '@/api/manage'

export default {
  name: 'GraduationCeremonyRecord',
  components: {},
  props: {
    // 1. 对象数据
    object: Object,
    // 通用禁用标识
    disabled: {
      type: Boolean,
      default: true,
      required: false,
    },
  },
  data() {
    return {
      model: this.object,
      validatorRules: {},
      url: {
        edit: '/graduation/graduationCeremony/edit',
      },
    }
  },
  methods: {
    handleSubmit(param) {
      const that = this
      param.status = '3'
      putAction(that.url.edit, param).then((res) => {
        that.$message.success('提交成功')
        that.object.status = '3'
        that.$emit('change', that.object)
      })
    },
  },
}
</script>

<style lang="less" scoped>
</style>