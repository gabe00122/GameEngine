#version 150

const float size = 1;
const float halfSize = size / 2;
const float c = 0.288675;
const float m = c * 2;

uniform sampler2D u_texture;

in vec4 v_color;
in vec2 v_texCoords;


ivec2 toHex(in vec2 position){
    ivec2 grid = ivec2(position / size);

    bool rowIsOdd = mod(grid.y, 2) == 1;
    if (rowIsOdd){
        grid.x = int((position.x - halfSize) / size);
    }

    vec2 rel = vec2(position.x - (grid.x * size), position.y - (grid.y * size));
    if(rowIsOdd){
        rel.x -= halfSize;
    }

    if(rel.y < (-m *rel.x) + c){
        grid.y -= 1;
        if(!rowIsOdd){
            grid.x -= 1;
        }
    }
    else if(rel.y < (m * rel.x) - c){
        grid.y -= 1;
        if(rowIsOdd){
            grid.x += 1;
        }
    }

    return(grid);
}


void main()
{
    gl_FragColor = v_color * texelFetch(u_texture, toHex(v_texCoords * textureSize(u_texture, 0)), 0);
}