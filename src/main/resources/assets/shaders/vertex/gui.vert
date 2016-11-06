#version 400 core

in vec2 position;

out vec2 texCoords;

uniform mat4 transformationMatrix;

void main(void) {
  
  gl_Position = vec4(position, 1, 0) * transformationMatrix;
  texCoords = vec2((position.x + 1) / 2, 1 - (position.y + 1) / 2);
  
}
