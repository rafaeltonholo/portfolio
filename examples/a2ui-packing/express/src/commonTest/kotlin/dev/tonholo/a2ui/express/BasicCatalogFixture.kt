package dev.tonholo.a2ui.express

// Trimmed from the A2UI v0.9 basic catalog, https://a2ui.org/specification/v0_9_1/catalogs/basic/catalog.json (Apache-2.0).
internal val basicCatalogSchema = $$"""
{
  "catalogId": "https://a2ui.org/specification/v0_9/catalogs/basic/catalog.json",
  "components": {
    "Text": {
      "allOf": [
        { "$ref": "common_types.json#/$defs/ComponentCommon" },
        {
          "properties": {
            "component": { "const": "Text" },
            "text": { "$ref": "common_types.json#/$defs/DynamicString" },
            "variant": { "type": "string", "enum": ["h1", "h2", "h3", "h4", "h5", "caption", "body"] }
          },
          "required": ["component", "text"]
        }
      ]
    },
    "Column": {
      "allOf": [
        { "$ref": "common_types.json#/$defs/ComponentCommon" },
        {
          "properties": {
            "component": { "const": "Column" },
            "children": { "$ref": "common_types.json#/$defs/ChildList" },
            "justify": { "type": "string" },
            "align": { "type": "string" }
          },
          "required": ["component", "children"]
        }
      ]
    },
    "Card": {
      "allOf": [
        { "$ref": "common_types.json#/$defs/ComponentCommon" },
        {
          "properties": {
            "component": { "const": "Card" },
            "child": { "$ref": "common_types.json#/$defs/ComponentId" }
          },
          "required": ["component", "child"]
        }
      ]
    },
    "Button": {
      "allOf": [
        { "$ref": "common_types.json#/$defs/ComponentCommon" },
        {
          "properties": {
            "component": { "const": "Button" },
            "child": { "$ref": "common_types.json#/$defs/ComponentId" },
            "variant": { "type": "string", "enum": ["default", "primary", "borderless"] },
            "action": { "$ref": "common_types.json#/$defs/Action" }
          },
          "required": ["component", "child", "action"]
        }
      ]
    },
    "CheckBox": {
      "allOf": [
        { "$ref": "common_types.json#/$defs/ComponentCommon" },
        {
          "properties": {
            "component": { "const": "CheckBox" },
            "label": { "$ref": "common_types.json#/$defs/DynamicString" },
            "value": { "$ref": "common_types.json#/$defs/DynamicBoolean" }
          },
          "required": ["component", "label", "value"]
        }
      ]
    }
  }
}
"""

internal val basicCatalog = ExpressCatalog.fromSchema(basicCatalogSchema)
