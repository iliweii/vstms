<template>
  <a-card :bordered="false">
    <j-file-upload
      ref="jFileUpload"
      :disabled="disabled"
      businessType="research_plan"
      :objectId="object.id"
      dict="file_research_plan"
      label=""
      text="点击添加调研方案"
      :labelColumn="{ span: 0 }"
      :wrapperColumn="{ span: 24 }"
    />
    <!-- 完成按钮 -->
    <div>
      <a-button @click="() => handleSubmit(object.id)" type="primary" :disabled="disabled" icon="check"
        >完成调研方案上传</a-button
      >
      <div class="ant-form-extra" :style="{ fontSize: '12px' }">完成后方可执行后续步骤（调研过程），完成后不可撤销</div>
    </div>
  </a-card>
</template>

<script>
import { putAction } from '@/api/manage'

export default {
  name: 'ResearchPlanModal',
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
      url: {
        edit: '/research/researchReq/edit',
      },
    }
  },
  methods: {
    handleSubmit(id) {
      const that = this
      putAction(that.url.edit, { id, status: 2 }).then((res) => {
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