#version 400 core

in vec3 position;
in vec2 texCoords;
in vec3 normal;

out vec2 pass_texCoords;
out vec3 surfaceNormal;
out vec3 toLightVec;
out vec3 toCameraVec;
out float visibility;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPos;

uniform float fogDensity;
uniform float fogGradient;

void main(void) {
  
  vec4 worldPos = transformationMatrix * vec4(position, 1.0);
  vec4 posRelativeToCam = viewMatrix * worldPos;
  
  gl_Position = projectionMatrix * posRelativeToCam;
  pass_texCoords = texCoords;
  
  surfaceNormal = (transformationMatrix * vec4(normal, 0.0)).xyz;
  toLightVec = lightPos - worldPos.xyz;
  toCameraVec = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPos.xyz;
  
  float distance = length(posRelativeToCam.xyz);
  visibility = exp(-pow(distance * fogDensity, fogGradient));
  visibility = clamp(visibility, 0.0, 1.0);
  
}
