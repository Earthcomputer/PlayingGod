#version 400 core

in vec2 pass_texCoords;
in vec3 surfaceNormal;
in vec3 toLightVec;
in vec3 toCameraVec;
in float visibility;

out vec4 out_Color;

uniform sampler2D texSampler;
uniform vec3 lightColor;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;

void main(void) {
  
  vec3 unitNormal = normalize(surfaceNormal);
  vec3 unitLightVec = normalize(toLightVec);
  vec3 unitCameraVec = normalize(toCameraVec);
  
  float nDotl = dot(unitNormal, unitLightVec);
  float brightness = max(nDotl, 0.2);
  vec3 diffuse = brightness * lightColor;
  
  vec3 lightDirection = -unitLightVec;
  vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
  
  float specularFactor = dot(reflectedLightDirection, unitCameraVec);
  specularFactor = max(specularFactor, 0.0);
  float dampedFactor = pow(specularFactor, shineDamper);
  vec3 finalSpecular = dampedFactor * reflectivity * lightColor;
  
  vec4 textureColor = texture(texSampler, pass_texCoords);
  if (textureColor.a < 0.5) {
    discard;
  }
  
  out_Color = vec4(diffuse, 1.0) * textureColor + vec4(finalSpecular, 1.0);
  out_Color = mix(vec4(skyColor, 1.0), out_Color, visibility);
  
}
