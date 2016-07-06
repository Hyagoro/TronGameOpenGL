package ui;

import modele.Direction;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.util.Color;

import java.nio.FloatBuffer;

public abstract class Bloc {
    private double x;
    private double y;
    private double z;

    private int taille;
    private Color color;

    private Direction direction;

    private boolean once = true;
    private int vbo_vertex_handle;
    private int vbo_color_handle;

    public Bloc(double x, double y, Color color) {
        this.x = x;
        this.y = y;
        this.z = 0;
        this.taille = 10;
        this.color = color;
    }

    public Bloc(double x, double y, double z, Color color) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.taille = 10;
        this.color = color;
    }

    private void drawVertices(int vertices, int vertex_size, int color_size) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo_vertex_handle);
        GL11.glVertexPointer(vertex_size, GL11.GL_FLOAT, 0, 0L);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo_color_handle);
        GL11.glColorPointer(color_size, GL11.GL_FLOAT, 0, 0L);

        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);

        GL11.glDrawArrays(GL11.GL_QUADS, 0, vertices);

        GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
    }

    protected void deleteVBO() {
        GL15.glDeleteBuffers(vbo_vertex_handle);
        GL15.glDeleteBuffers(vbo_color_handle);
    }

    public static FloatBuffer floatBuffer(float a, float b, float c, float d) {
        float data[] = new float[]{a, b, c, d};
        FloatBuffer fb = BufferUtils.createFloatBuffer(data.length);
        fb.put(data);
        fb.flip();
        return fb;
    }

    private void initialiserVBOTexture(int vertices, int vertex_size, int color_size) {
        float ratioSombre = 0.75f;

        if (once) {
            // GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE,
            // floatBuffer(1.0f, 0.0f, 0.0f, 1.0f));
            // GL11.glMaterial(GL11.GL_FRONT, GL11.GL_EMISSION,
            // floatBuffer(0.0f, 0.0f, 0.0f, 1.0f));
            // GL11.glMaterial(GL11.GL_BACK, GL11.GL_AMBIENT_AND_DIFFUSE,
            // floatBuffer(0.0f, 0.0f, 1.0f, 1.0f));
            // GL11.glMaterial(GL11.GL_BACK, GL11.GL_EMISSION, floatBuffer(0.0f,
            // 0.0f, 0.0f, 1.0f));
            // GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR,
            // floatBuffer(0.5f, 0.5f, 0.5f, 1.0f));
            //
            // GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, 10);

            float r = (float) color.getRed() / 255f;
            float g = (float) color.getGreen() / 255f;
            float b = (float) color.getBlue() / 255f;

            float x = (float) this.x;
            float y = (float) this.y;
            float z = (float) this.z;

            FloatBuffer vertex_data = BufferUtils.createFloatBuffer(vertices * vertex_size);
            // nord
            vertex_data.put(new float[]{x + taille / 2, y + taille / 2, z + taille / 2});
            vertex_data.put(new float[]{x + taille / 2, y + taille / 2, z - taille / 2});
            vertex_data.put(new float[]{x - taille / 2, y + taille / 2, z - taille / 2});
            vertex_data.put(new float[]{x - taille / 2, y + taille / 2, z + taille / 2});

            // est
            vertex_data.put(new float[]{x + taille / 2, y - taille / 2, z + taille / 2});
            vertex_data.put(new float[]{x + taille / 2, y - taille / 2, z - taille / 2});
            vertex_data.put(new float[]{x + taille / 2, y + taille / 2, z - taille / 2});
            vertex_data.put(new float[]{x + taille / 2, y + taille / 2, z + taille / 2});

            // sud
            vertex_data.put(new float[]{x - taille / 2, y - taille / 2, z + taille / 2});
            vertex_data.put(new float[]{x - taille / 2, y - taille / 2, z - taille / 2});
            vertex_data.put(new float[]{x + taille / 2, y - taille / 2, z - taille / 2});
            vertex_data.put(new float[]{x + taille / 2, y - taille / 2, z + taille / 2});

            // ouest
            vertex_data.put(new float[]{x - taille / 2, y + taille / 2, z + taille / 2});
            vertex_data.put(new float[]{x - taille / 2, y + taille / 2, z - taille / 2});
            vertex_data.put(new float[]{x - taille / 2, y - taille / 2, z - taille / 2});
            vertex_data.put(new float[]{x - taille / 2, y - taille / 2, z + taille / 2});

            // dessous
            vertex_data.put(new float[]{x + taille / 2, y + taille / 2, z - taille / 2});
            vertex_data.put(new float[]{x + taille / 2, y - taille / 2, z - taille / 2});
            vertex_data.put(new float[]{x - taille / 2, y - taille / 2, z - taille / 2});
            vertex_data.put(new float[]{x - taille / 2, y + taille / 2, z - taille / 2});

            // dessus
            vertex_data.put(new float[]{x + taille / 2, y - taille / 2, z + taille / 2});
            vertex_data.put(new float[]{x + taille / 2, y + taille / 2, z + taille / 2});
            vertex_data.put(new float[]{x - taille / 2, y + taille / 2, z + taille / 2});
            vertex_data.put(new float[]{x - taille / 2, y - taille / 2, z + taille / 2});

            vertex_data.flip();

            FloatBuffer color_data = BufferUtils.createFloatBuffer(vertices * color_size);

            // bleu foncé
            color_data.put(new float[]{ratioSombre * r, ratioSombre * g, ratioSombre * b});
            color_data.put(new float[]{ratioSombre * r, ratioSombre * g, ratioSombre * b});
            color_data.put(new float[]{ratioSombre * r, ratioSombre * g, ratioSombre * b});
            color_data.put(new float[]{ratioSombre * r, ratioSombre * g, ratioSombre * b});

            // bleu foncé
            color_data.put(new float[]{ratioSombre * r, ratioSombre * g, ratioSombre * b});
            color_data.put(new float[]{ratioSombre * r, ratioSombre * g, ratioSombre * b});
            color_data.put(new float[]{ratioSombre * r, ratioSombre * g, ratioSombre * b});
            color_data.put(new float[]{ratioSombre * r, ratioSombre * g, ratioSombre * b});

            // bleu foncé
            color_data.put(new float[]{ratioSombre * r, ratioSombre * g, ratioSombre * b});
            color_data.put(new float[]{ratioSombre * r, ratioSombre * g, ratioSombre * b});
            color_data.put(new float[]{ratioSombre * r, ratioSombre * g, ratioSombre * b});
            color_data.put(new float[]{ratioSombre * r, ratioSombre * g, ratioSombre * b});

            // bleu foncé
            color_data.put(new float[]{ratioSombre * r, ratioSombre * g, ratioSombre * b});
            color_data.put(new float[]{ratioSombre * r, ratioSombre * g, ratioSombre * b});
            color_data.put(new float[]{ratioSombre * r, ratioSombre * g, ratioSombre * b});
            color_data.put(new float[]{ratioSombre * r, ratioSombre * g, ratioSombre * b});

            // bleu foncé
            color_data.put(new float[]{ratioSombre * r, ratioSombre * g, ratioSombre * b});
            color_data.put(new float[]{ratioSombre * r, ratioSombre * g, ratioSombre * b});
            color_data.put(new float[]{ratioSombre * r, ratioSombre * g, ratioSombre * b});
            color_data.put(new float[]{ratioSombre * r, ratioSombre * g, ratioSombre * b});

            // bleu foncé plus foncé
            color_data.put(new float[]{r, g, b});
            color_data.put(new float[]{r, g, b});
            color_data.put(new float[]{r, g, b});
            color_data.put(new float[]{r, g, b});

            color_data.flip();

            vbo_vertex_handle = GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo_vertex_handle);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertex_data, GL15.GL_STATIC_DRAW);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

            vbo_color_handle = GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo_color_handle);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, color_data, GL15.GL_STATIC_DRAW);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

            once = false;
        }
    }

    public void update() {
        // TODO MAJ des textures (a voir)
    }

    public void render() {
        int vertices = 24;
        int vertex_size = 3; // XYZ
        int color_size = 3; // RGB

        initialiserVBOTexture(vertices, vertex_size, color_size);
        drawVertices(vertices, vertex_size, color_size);
    }

    public void renderDynamic() {
        GL11.glBegin(GL11.GL_QUADS);

        float ratioSombre = 0.75f;
        float r = (float) color.getRed() / 255f;
        float g = (float) color.getGreen() / 255f;
        float b = (float) color.getBlue() / 255f;

        GL11.glColor3f(ratioSombre * r, ratioSombre * g, ratioSombre * b); // face rouge
        GL11.glVertex3d(x + taille / 2, y + taille / 2, z + taille / 2);
        GL11.glVertex3d(x + taille / 2, y + taille / 2, z - taille / 2);
        GL11.glVertex3d(x - taille / 2, y + taille / 2, z - taille / 2);
        GL11.glVertex3d(x - taille / 2, y + taille / 2, z + taille / 2);

        //
        GL11.glColor3b(color.getRedByte(), color.getGreenByte(), color.getBlueByte());
        GL11.glColor3f(ratioSombre * r, ratioSombre * g, ratioSombre * b); // face verte
        GL11.glVertex3d(x + taille / 2, y - taille / 2, z + taille / 2);
        GL11.glVertex3d(x + taille / 2, y - taille / 2, z - taille / 2);
        GL11.glVertex3d(x + taille / 2, y + taille / 2, z - taille / 2);
        GL11.glVertex3d(x + taille / 2, y + taille / 2, z + taille / 2);

        GL11.glColor3f(ratioSombre * r, ratioSombre * g, ratioSombre * b); // face bleue
        GL11.glVertex3d(x - taille / 2, y - taille / 2, z + taille / 2);
        GL11.glVertex3d(x - taille / 2, y - taille / 2, z - taille / 2);
        GL11.glVertex3d(x + taille / 2, y - taille / 2, z - taille / 2);
        GL11.glVertex3d(x + taille / 2, y - taille / 2, z + taille / 2);

        GL11.glColor3f(ratioSombre * r, ratioSombre * g, ratioSombre * b); // face jaune
        GL11.glVertex3d(x - taille / 2, y + taille / 2, z + taille / 2);
        GL11.glVertex3d(x - taille / 2, y + taille / 2, z - taille / 2);
        GL11.glVertex3d(x - taille / 2, y - taille / 2, z - taille / 2);
        GL11.glVertex3d(x - taille / 2, y - taille / 2, z + taille / 2);

        GL11.glColor3f(ratioSombre * r, ratioSombre * g, ratioSombre * b); // face cyan
        GL11.glVertex3d(x + taille / 2, y + taille / 2, z - taille / 2);
        GL11.glVertex3d(x + taille / 2, y - taille / 2, z - taille / 2);
        GL11.glVertex3d(x - taille / 2, y - taille / 2, z - taille / 2);
        GL11.glVertex3d(x - taille / 2, y + taille / 2, z - taille / 2);

        GL11.glColor3f(r, g, b);
        // GL11. glColor3f(1f,0,1f); //face magenta
        GL11.glVertex3d(x + taille / 2, y - taille / 2, z + taille / 2);
        GL11.glVertex3d(x + taille / 2, y + taille / 2, z + taille / 2);
        GL11.glVertex3d(x - taille / 2, y + taille / 2, z + taille / 2);
        GL11.glVertex3d(x - taille / 2, y - taille / 2, z + taille / 2);

        GL11.glEnd();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
