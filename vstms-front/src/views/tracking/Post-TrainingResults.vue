<template>
  <page-layout :title="title">
    <a-card :bordered="false">
      <j-file-upload
        ref="jFileUpload"
        :disabled="false"
        businessType="post-training-results_attach"
        objectId="post-training-results_attach"
        :labelColumn="{ span: 0 }"
        :wrapperColumn="{ span: 24 }"
        :extra="[
          {
            name: '附件',
            emit: 'attach',
          },
        ]"
        @attach="handleAttach"
      />
    </a-card>

    <post-trainingResult-attach ref="modalForm"></post-trainingResult-attach>
  </page-layout>
</template>

<script>
import PageLayout from '@/components/page/PageLayout'
import PostTrainingResultAttach from './modules/TrainingResultAttach.vue'

export default {
  name: 'Post-TrainingResults',
  components: {
    PageLayout,
    PostTrainingResultAttach,
  },
  data() {
    return {
      title: '训后成果',
    }
  },
  methods: {
    handleAttach(attach) {
      this.$refs.modalForm.title = '成果'
      this.$refs.modalForm.object = attach
      this.$refs.modalForm.disableSubmit = true
    },
  },
}
</script>

<style lang="less" scoped>
</style>