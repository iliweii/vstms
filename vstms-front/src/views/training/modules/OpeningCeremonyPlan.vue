<template>
  <a-card :bordered="false">
    <a-spin :spinning="false">
      <a-form-model ref="form" :model="model" :rules="validatorRules">
        <a-row>
          <a-col :md="12">
            <a-form-model-item :labelCol="{ span: 4 }" :wrapperCol="{ span: 18 }" prop="holdDate" label="举行日期">
              <a-date-picker v-model="model.holdDate" :disabled="disabled" :style="{ width: '100%' }" />
            </a-form-model-item>
          </a-col>
          <a-col :md="12">
            <a-form-model-item :labelCol="{ span: 4 }" :wrapperCol="{ span: 16 }" prop="referNum" label="参与人数">
              <a-input v-model="model.referNum" suffix="人" :disabled="disabled" />
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
        businessType="training_opening_ceremony_plan"
        :objectId="object.classNo"
        label="方案"
        text="点击添加开班典礼方案"
        :labelColumn="{ span: 2 }"
        :wrapperColumn="{ span: 20 }"
      />
    </a-spin>

    <!-- 完成按钮 -->
    <div :style="{ marginTop: '15px' }">
      <a-button @click="() => handleSubmit(model)" type="primary" :disabled="disabled" icon="check"
        >完成开班典礼方案上传</a-button
      >
      <div class="ant-form-extra" :style="{ fontSize: '12px' }">
        完成后方可执行后续步骤（开班典礼记录），完成后不可撤销
      </div>
    </div>
  </a-card>
</template>

<script>
import { putAction } from '@/api/manage'

export default {
  name: 'OpeningCeremonyPlan',
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
        edit: '/training/trainingOpeningCeremony/edit',
      },
    }
  },
  methods: {
    handleSubmit(param) {
      const that = this
      param.status = '2'
      putAction(that.url.edit, param).then((res) => {
        that.$message.success('提交成功')
        that.object.status = '2'
        that.$emit('change', that.object)
      })
    },
  },
}
</script>

<style lang="less" scoped>
</style>