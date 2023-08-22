---
name: Feature request
about: Suggest an idea for this project
title: ''
labels: enhancement
assignees: ''

---

name: "✨ Feature"
description: "어떤 기능인지 구체적으로 설명해주세요."
labels: "✨ Feature"
body:
- type: textarea
  attributes:
  label: ✨ Describe
  description: 새로운 기능에 대한 설명을 작성해 주세요.
  placeholder: 자세히 적을수록 좋습니다!
  validations:
  required: true
- type: textarea
  attributes:
  label: ✅ Tasks
  description: 해야 하는 일에 대한 Tasks를 작성해 주세요.
  placeholder: 최대한 세분화 해서 적어주세요!
  validations:
  required: true
- type: textarea
  attributes:
  label: 🕰️ Estimated Time of Completion
  description: 예상 소요 시간을 작성해 주세요.
- type: textarea
  attributes:
  label: 🙋🏻 More
  description: 더 하고 싶은 말이 있다면 작성해 주세요.

<!--
✅ Label을 설정하였는지 확인해주세요.
✅ Assignee를 지정하였는지 확인해주세요.
-->
