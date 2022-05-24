<template>
  <page-layout :title="title">
    <a-card :bordered="false">
      <j-file-upload
        ref="jFileUpload"
        :disabled="false"
        businessType="study_material"
        objectId="study_material"
        dict="file_study_material"
        text="上传学习资料"
        :labelColumn="{ span: 0 }"
        :wrapperColumn="{ span: 24 }"
      />
    </a-card>
  </page-layout>
</template>

<script>
import PageLayout from '@/components/page/PageLayout'

export default {
  name: 'StudyMaterial',
  components: {
    PageLayout,
  },
  data() {
    return {
      title: '学习资料',
    }
  },
}
</script>

<style lang="less" scoped>
</style>