import struct

class Marshalling:


    @classmethod
    def marshall(cls,data: tuple) -> bytearray:
        marshalled_data = bytearray()
        for item in data:
            marshalled_data += cls.toBytes(item)

        return marshalled_data

    @classmethod
    def unmarshall(cls,buffer: bytearray,paraTypeOrder:tuple) -> list:
        unmarshall_data = []
        ptr = 0 
        for data_type in paraTypeOrder:
            if data_type is int:
                unmarshall_data.append(struct.unpack('>i',buffer[ptr:ptr+4])[0])
                ptr +=4
            elif data_type is float:
                unmarshall_data.append(struct.unpack('>f',buffer[ptr:ptr+4])[0])
                ptr +=4
            elif data_type is str:
                length = struct.unpack('>i',buffer[ptr:ptr+4])[0]
                print(length)
                ptr +=4 
                unmarshall_data.append(buffer[ptr:ptr+length].decode('utf-8'))
                ptr +=length                

        return unmarshall_data

    @classmethod
    def toBytes(cls, data)-> bytes:        
        # < little-endian
        # > big-endian

        if isinstance(data,int):
            return struct.pack('>i', data)
        elif isinstance(data,float):
            return struct.pack('>f', data)
        elif isinstance(data,str):
            return struct.pack('>i', len(data)) + bytes(data.encode('utf-8'))
        else:
            raise Exception(f'Unsupported data type {type(data)} for marshalling')

if __name__ == '__main__':
    msg_byteArray = Marshalling.marshall((0,123,456.789,"abcdefg"))
    msg = Marshalling.unmarshall(msg_byteArray,(int,int,float,str))

    print(msg)