<template>
  <page-layout :title="title">
    <a-card :bordered="false">
      <j-file-upload
        ref="jFileUpload"
        :disabled="false"
        businessType="declare_form"
        objectId="declare_form"
        dict="file_declare_form"
        :labelColumn="{ span: 0 }"
        :wrapperColumn="{ span: 24 }"
      />
    </a-card>
  </page-layout>
</template>

<script>
  import PageLayout from '@/components/page/PageLayout'

  export default {
    name: 'ProjectDeclaration',
    components: {
      PageLayout
    },
    data () {
      return {
        title: '项目申报书'
      }
    }
  }
</script>

<style lang="less" scoped>
</style>