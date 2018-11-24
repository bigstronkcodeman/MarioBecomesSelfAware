class Camera
{
	int offset;
	boolean sleep;

	Camera(Camera copyCam)
	{
		offset = copyCam.offset;
		sleep = copyCam.sleep;
	}

	Camera() 
	{
		offset = 0;
		sleep = false;
	}

	Camera(Json obj) 
	{
		offset = (int)obj.getLong("offset");
		sleep = obj.getBool("sleep");
	}

	void increment() 
	{
		if (!sleep) 
		{
			offset += 6;
		}
	}

	void decrement() 
	{
		if (!sleep) 
		{
			offset -= 6;
		}
	}

	void tuckIn() 
	{
		sleep = true;
	}

	void wakeUp() 
	{
		sleep = false;
	}

	boolean isAsleep() 
	{
		return sleep;
	}

	int getOffset() 
	{
		return offset;
	}

	Json marshall() 
	{
		Json obj = Json.newObject();
		obj.add("offset", offset);
		obj.add("sleep", sleep);
		return obj;
	}
} 