const fs = require('fs')
const path = require('path')

const dir = path.join(__dirname, '../src/main/resources/data/gunsrpg/recipes/gunsmith')
let wrapped = 0
let skipped = 0

for (const file of fs.readdirSync(dir)) {
  if (!file.endsWith('.json')) continue
  const full = path.join(dir, file)
  const raw = fs.readFileSync(full, 'utf8').replace(/^\uFEFF/, '')
  const json = JSON.parse(raw)
  if (json.type === 'forge:conditional') {
    skipped++
    continue
  }
  const body = JSON.stringify(json)
  if (!body.includes('mmt:')) {
    skipped++
    continue
  }
  const wrappedRecipe = {
    type: 'forge:conditional',
    recipes: [
      {
        conditions: [{ type: 'forge:mod_loaded', modid: 'mmt' }],
        recipe: json,
      },
    ],
  }
  fs.writeFileSync(full, JSON.stringify(wrappedRecipe, null, 2) + '\n')
  wrapped++
}

console.log(`wrapped=${wrapped} skipped=${skipped}`)
